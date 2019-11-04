package space.xrapid.api;

import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Stats;
import space.xrapid.service.ExchangeToExchangePaymentService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
public class PaymentsRest {

    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @GET
    @Produces("application/json")
    public List<ExchangeToExchangePayment> getLastPayments() {
        return exchangeToExchangePaymentService.getLasts().stream().sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime)).collect(Collectors.toList());
    }

    @GET
    @Produces("application/json")
    @Path("/stats")
    public Stats getStats() {
        return exchangeToExchangePaymentService.calculateStats();
    }
}
