package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.xumm.*;
import space.xrapid.domain.xumm.webhook.WebHook;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class XummService {
    private final String apiBase = "https://xumm.app/api/v1/platform/payload";

    @Value("${xumm.api.key}")
    private String apiKey;

    @Value("${xumm.api.secret}")
    private String secret;

    @Value("${xumm.api.destination}")
    private String destination;

    @Autowired
    private XrpLedgerService xrpLedgerService;

    private Map<String, Long> availableTags = new HashMap<>();

    private Map<String, String> status = new HashMap<>();

    private Map<String, Double> amounts = new HashMap<>();

    private Map<String, Long> tags = new HashMap<>();


    private RestTemplate restTemplate = new RestTemplate();

    public PaymentRequestInformation requestPayment(double amount, String currency, String instruction) {

        HttpHeaders headers = getHeaders();

        long destinationTag = getNextDestinationTag();

        ResponseEntity<Response> response = restTemplate.exchange(apiBase,
                HttpMethod.POST, new HttpEntity(buildPayment(amount, currency, instruction, destinationTag), headers), Response.class);

        String id = response.getBody().getUuid();

        amounts.put(id, amount);
        tags.put(id, destinationTag);

        return PaymentRequestInformation.builder().paymentId(response.getBody().getUuid()).qrCodeUrl(response.getBody().getRefs().getQrPng()).build();
    }

    public String verifyPayment(String id) {

        if (!status.containsKey(id)) {
            return "WAITING";
        }

        return status.get(id);
    }

    public synchronized Long getNextDestinationTag() {

        if (availableTags.isEmpty()) {
            return 0l;
        }

        return availableTags.values().stream().mapToLong(Long::longValue).max().getAsLong() + 1;
    }

    public void updatePaymentStatus(WebHook webHook) {
        String id = webHook.getPayloadResponse().getPayloadUuidv4();
        if (webHook.getPayloadResponse().getSigned() == null || !webHook.getPayloadResponse().getSigned()) {
            status.put(id, "REJECTED");
        } else if (webHook.getPayloadResponse().getSigned() != null && webHook.getPayloadResponse().getSigned()) {
            boolean paymentConfirmed = xrpLedgerService.verifyPaymentByDestinationAndDestinationTag(destination, tags.get(id), amounts.get(id));
            if (paymentConfirmed) {
                status.put(id, "REJECTED");

            } else {
                status.put(id, "SIGNED");
            }

        }

        availableTags.remove(id);
        tags.remove(id);
        amounts.remove(id);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        headers.add("x-api-key", apiKey);
        headers.add("x-api-secret", secret);
        headers.add("authorization", "Bearer ");
        return headers;
    }

    private Payload buildPayment(double amount, String currency, String instruction, long destinationTag) {
        return
                Payload.builder().txjson(XummPayment.builder()
                        .transactionType("Payment")
                        .destination(destination)
                        .destinationTag(destinationTag)
                        .amount(Amount.builder()
                                .issuer(destination)
                                .value(amount)
                                .currency(currency).build()).build())
                        .customMeta(CustomMeta.builder()
                                .instruction(instruction).build())
                        .build();
    }


    private void buildHeaders(HttpHeaders headers) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        headers.add("x-api-key", apiKey);
        headers.add("x-api-secret", secret);
        headers.add("authorization", "Bearer ");
    }
}
