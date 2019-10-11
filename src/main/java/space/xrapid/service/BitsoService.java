package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.bitso.BitsoPayments;
import space.xrapid.domain.bitso.Trade;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BitsoService {

    private String url = "https://api.bitso.com/v3/trades/?book=xrp_mxn&sort=desc&limit=100";

    private RestTemplate restTemplate = new RestTemplate();

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private Integer marker;

    public List<Trade> fetchPayments(OffsetDateTime begin) {
        List<Trade> payments = new ArrayList<>();
        List<Trade> currentPayments = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        String getUrl = marker != null ? url + "&marker=" + marker : url;

        ResponseEntity<BitsoPayments> response = restTemplate.exchange(getUrl,
                HttpMethod.GET, entity, BitsoPayments.class);


        if (response.getBody().getSuccess() && response.getBody() != null) {
            currentPayments = getPayments(begin, response);

            payments.addAll(currentPayments);
        }

        while (currentPayments.size() == 100) {
            marker = getMarker(begin, response);

            if (marker == null || response.getBody().getPayment().size() < 100) {
                break;
            }

            response = restTemplate.exchange(url + "&marker=" + marker,
                    HttpMethod.GET, entity, BitsoPayments.class);

            if (response.getBody().getSuccess() && response.getBody() != null) {
                currentPayments = getPayments(begin, response);
                payments.addAll(currentPayments);
            }

        }

        return payments;

    }

    private Integer getMarker(OffsetDateTime begin, ResponseEntity<BitsoPayments> response) {
        return response.getBody().getPayment().stream()
                .filter(p -> "sell".equals(p.getMakerSide()))
                .filter(p -> begin.isBefore(OffsetDateTime.parse(p.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter)))
                .map(Trade::getTid)
                .sorted()
                .findFirst()
                .orElse(null);
    }

    private List<Trade> getPayments(OffsetDateTime begin, ResponseEntity<BitsoPayments> response) {
        return response.getBody().getPayment().stream()
                .filter(p -> "sell".equals(p.getMakerSide()))
                .filter(p -> begin.isBefore(OffsetDateTime.parse(p.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter)))
                .sorted(Comparator.comparing(Trade::getCreatedAt))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

}
