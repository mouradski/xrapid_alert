package space.xrapid.api;

import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.repository.XrapidPaymentRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
public class PaymentsRest {

    @Autowired
    private XrapidPaymentRepository repository;

    @GET
    @Produces("application/json")
    public List<ExchangeToExchangePayment> getLasts() {
        return repository.getLasts().stream()
                .limit(15).collect(Collectors.toList()).stream()
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .collect(Collectors.toList());
    }
}
