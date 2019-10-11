package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.bitso.Bid;
import space.xrapid.domain.bitso.BitsoResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class BitsoService {

    private String url = "https://api.bitso.com/v3/order_book/?book=xrp_mxn&aggregate=false";

    private RestTemplate restTemplate = new RestTemplate();

    public List<Bid> fetchPayments() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<BitsoResponse> response = restTemplate.exchange(url,
                HttpMethod.GET, entity, BitsoResponse.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            return response.getBody().getPayload().getBids();
        }

        log.warn("Unable to retrieve XRP_MXN bids from BITSO", response.getStatusCode());
        return new ArrayList<>();

    }
}
