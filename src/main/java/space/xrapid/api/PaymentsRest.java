package space.xrapid.api;

import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.Currency;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Stats;
import space.xrapid.service.ApiKeyService;
import space.xrapid.service.ExchangeToExchangePaymentService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
        return exchangeToExchangePaymentService.getLasts();
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public List<ExchangeToExchangePayment> search(@QueryParam("key") String apiKey, @QueryParam("from") Long from, @QueryParam("to") Long to, @QueryParam("source") Currency source, @QueryParam("destination") Currency destination) {
        apiKeyService.validateKey(apiKey);

        return exchangeToExchangePaymentService.search(from, to, source, destination);
    }

    @GET
    @Produces("application/json")
    @Path("/stats")
    public Stats getStats() {
        return exchangeToExchangePaymentService.calculateStats();
    }
}
