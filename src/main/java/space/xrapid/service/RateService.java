package space.xrapid.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.exchange.bitstamp.Ticker;

import java.util.Arrays;

@Service
public class RateService {

    private final String apiUrl = "https://www.bitstamp.net/api/v2/ticker/xrpusd";
    private RestTemplate restTemplate = new RestTemplate();

    public double getXrpUsdRate() {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Ticker> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Ticker.class);

        return response.getBody().getLast();
    }

    private HttpEntity getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return new HttpEntity("parameters", headers);
    }
}
