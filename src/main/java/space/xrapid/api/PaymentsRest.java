package space.xrapid.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.*;
import space.xrapid.service.ApiKeyService;
import space.xrapid.service.ExchangeToExchangePaymentService;

import javax.ws.rs.*;
import java.util.List;

@Path("/payments")
public class PaymentsRest {

    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ApiKeyService apiKeyService;

    @Value("${api.proxy:false}")
    private boolean proxy;

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
    public OdlPaymentsResponse search(@QueryParam("key") String apiKey, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("source") Currency source, @QueryParam("destination") Currency destination, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        apiKeyService.validateKey(apiKey);
        return exchangeToExchangePaymentService.search(from, to, source, destination, size == null ? 300 : size, page == null ? 0 : page);
    }

    @GET
    @Produces("application/json")
    @Path("/stats")
    public Stats getStats(@QueryParam("key") String apiKey, @QueryParam("size") Integer size) {

        if (size != null) {
            apiKeyService.validateKey(apiKey);
        }

        if (size == null) {
            size = 21;
        }
        return exchangeToExchangePaymentService.calculateStats(size);
    }

    @GET
    @Produces("application/json")
    @Path("/stats/all")
    public GlobalStats getAllStats(@QueryParam("key") String apiKey) {
        apiKeyService.validateKey(apiKey);
        return exchangeToExchangePaymentService.calculateGlobalStats(false);
    }

    @POST
    @Consumes("application/json")
    public void push(@QueryParam("key") String key, Payment payment) {
        if (proxy) {
            apiKeyService.validateMasterKey(key);
            messagingTemplate.convertAndSend("/top/odl", payment);
        }
    }

}
