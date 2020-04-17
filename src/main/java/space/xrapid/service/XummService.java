package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.ApiKey;
import space.xrapid.domain.xumm.*;
import space.xrapid.domain.xumm.webhook.WebHook;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class XummService {
    private final String apiBase = "https://xumm.app/api/v1/platform/payload";

    @Value("${xumm.api.key}")
    private String apiKey;

    @Value("${xumm.api.secret}")
    private String secret;

    @Value("${xumm.api.destination}")
    private String destination;

    @Autowired
    private ApiKeyService apiKeyService;


    private Map<String, String> status = new HashMap<>();

    private Map<String, Double> amounts = new HashMap<>();

    private Map<String, String> keys = new HashMap<>();
    private Map<String, Date> expirations = new HashMap<>();
    private Map<String, ApiKey> renewedKeys = new HashMap<>();




    private RestTemplate restTemplate = new RestTemplate();

    @Transactional(readOnly = true)
    public PaymentRequestInformation requestPayment(double amount, String currency, Integer days, String key) {

        Date expiration;
        ApiKey apiKey = null;
        if (key != null) {
            apiKey = apiKeyService.getApiKey(key);

            expiration = new Date(apiKey.getExpiration().getTime() + days * 24 * 60 * 60 * 1000);
        } else {
            expiration = new Date(new Date().getTime() + days * 24 * 60 * 60 * 1000);
        }

        String instruction = "Your key will be available after payment confirmation. Your key will expire : _EXPIRE_.".replace("_EXPIRE_", expiration.toString());

        HttpHeaders headers = getHeaders();


        double scale = Math.pow(10, 6);
        double randomAmount =  Math.round((Math.random() / 30) * scale) / scale;

        double finalAmount = amount + randomAmount;

        ResponseEntity<Response> response = restTemplate.exchange(apiBase,
                HttpMethod.POST, new HttpEntity(buildPayment(finalAmount, currency, instruction), headers), Response.class);

        String id = response.getBody().getUuid();

        if (apiKey != null) {
           keys.put(id, key);
        }

        amounts.put(id, finalAmount);
        expirations.put(id, expiration);

        return PaymentRequestInformation.builder().paymentId(id).qrCodeUrl(response.getBody().getRefs().getQrPng()).build();
    }

    public String verifyPayment(String id) {

        if (renewedKeys.containsKey(id)) {
            return "SIGNED";
        }

        if (!status.containsKey(id)) {
            return "WAITING";
        }

        return status.get(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePaymentStatus(WebHook webHook) {
        String id = webHook.getPayloadResponse().getPayloadUuidv4();
        if (webHook.getPayloadResponse().getSigned() == null || !webHook.getPayloadResponse().getSigned()) {
            status.put(id, "REJECTED");
        } else if (webHook.getPayloadResponse().getSigned() != null && webHook.getPayloadResponse().getSigned()) {
            HttpHeaders headers = getHeaders();
            ResponseEntity<XummPaymentStatus> response = restTemplate.exchange(apiBase + "/" + id,
                    HttpMethod.GET, new HttpEntity("", headers), XummPaymentStatus.class);

            if (response.getBody().getResponse().getAdditionalProperties().containsKey("dispatched_result")
                    && response.getBody().getResponse().getAdditionalProperties().get("dispatched_result").toString().startsWith("tec")) {
                status.put(id, "REJECTED");
            } else {
                if (keys.get(id) != null) {
                    ApiKey apiKey = apiKeyService.getApiKey(keys.get(id));

                    renewedKeys.put(id, apiKeyService.renewKey(keys.get(id), expirations.get(id)));
                }
                status.put(id, "SIGNED");
            }
        }

        amounts.remove(id);
    }

    public ApiKey getRenewedApiKey(String id) {
        return renewedKeys.get(id);
    }

    public ApiKey getKey(String id) {
        return apiKeyService.generateApiKey(expirations.get(id));
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

    private Payload buildPayment(double amount, String currency, String instruction) {
        return
                Payload.builder().txjson(XummPayment.builder()
                        .transactionType("Payment")
                        .destination(destination)
                        .amount(Amount.builder()
                                .issuer(destination)
                                .value(amount)
                                .currency(currency).build()).build())
                        .customMeta(CustomMeta.builder()
                                .instruction(instruction).build())
                        .build();
    }
}
