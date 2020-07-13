package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.domain.ripple.Payments;
import space.xrapid.domain.xumm.Payload;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class XrpLedgerService {

    private String xrplApiUrl = "https://data.ripple.com/v2/payments/xrp?type=Payment&start={START}&end={END}&limit=1000&descending=false";

    private RestTemplate restTemplate = new RestTemplate();

    public List<Payment> fetchOdlCandidatePayments(OffsetDateTime startOffset, OffsetDateTime endOffset, boolean odlCandidateOnly) {
        List<Payment> payments = new ArrayList<>();

        String endAsString = endOffset.format(DateTimeFormatter.ISO_INSTANT);
        String startAsString = startOffset.format(DateTimeFormatter.ISO_INSTANT);

        String url = xrplApiUrl
                .replace("{START}", startAsString)
                .replace("{END}", endAsString);

        String urlWithMarker = url + "&marker={MARKER}";

        boolean hasNext = true;

        while (hasNext) {
            ResponseEntity<Payments> response = restTemplate.exchange(url,
                    HttpMethod.GET, null, Payments.class);

            hasNext = hasNext(response);

            if (hasNext) {
                url = urlWithMarker.replace("{MARKER}", response.getBody().getMarker());
            }

            payments.addAll(response.getBody().getPayments().stream()
                    .filter(this::filterRoundAmount)
                    .filter(p -> p.getAmount() > 20)
                    .filter(filterPayments(odlCandidateOnly))
                    .collect(Collectors.toList()));

        }


        return payments;
    }

    private boolean filterRoundAmount(Payment payment) {
        return payment.getAmount() % 1 != 0;
    }

    private Predicate<Payment> filterPayments(boolean odlCandidateOnly) {
        return p -> !odlCandidateOnly || (Exchange.byAddress(p.getSource()) != null && Exchange.byAddress(p.getDestination()) != null);
    }

    private boolean hasNext(ResponseEntity<Payments> response) {
        return (response.getBody().getMarker() != null && !response.getBody().getMarker().isEmpty());
    }
}
