package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import space.xrapid.domain.XrpTrade;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public interface TradeService {
    List<XrpTrade> fetchTrades(OffsetDateTime begin);

    default HttpEntity getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return  new HttpEntity("parameters", headers);
    }
}
