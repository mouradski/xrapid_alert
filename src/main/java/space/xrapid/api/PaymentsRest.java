package space.xrapid.api;

import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Stats;
import space.xrapid.service.ApiKeyService;
import space.xrapid.service.ExchangeToExchangePaymentService;

import javax.ws.rs.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
public class PaymentsRest {

    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    private ApiKeyService apiKeyService;

    @GET
    @Produces("application/json")
    public List<ExchangeToExchangePayment> getPayments(@QueryParam("key") String apiKey, @QueryParam("from") Long from, @QueryParam("to") Long to) {
        if (apiKey != null && from != null && to != null) {
            apiKeyService.validateKey(apiKey);
            return exchangeToExchangePaymentService.getPayments(from, to);
        }
        return exchangeToExchangePaymentService.getLasts().stream().sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime)).collect(Collectors.toList());
    }

    @GET
    @Produces("application/json")
    @Path("/stats")
    public Stats getStats() {
        return exchangeToExchangePaymentService.calculateStats();
    }
}
