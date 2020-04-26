package space.xrapid.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import space.xrapid.domain.*;
import space.xrapid.exception.UnauthorizedException;
import space.xrapid.service.ApiKeyService;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.util.CsvHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
@Slf4j
@Component
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
    public OdlPaymentsResponse search(@QueryParam("key") String apiKey, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("source") Currency source, @QueryParam("destination") Currency destination, @QueryParam("tag") Long tag, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        if (apiKey == null) {
            throw new UnauthorizedException();
        }
        apiKeyService.validateKey(apiKey);
        return exchangeToExchangePaymentService.search(from, to, source, destination, tag, size == null ? 300 : size, page == null ? 1 : page);
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
        if (apiKey == null) {
            throw new UnauthorizedException();
        }
        apiKeyService.validateKey(apiKey);
        return exchangeToExchangePaymentService.calculateGlobalStats(false);
    }

    @GET
    @Path("/search/csv")
    @Produces("text/csv")
    public Response csv(@QueryParam("key") String apiKey, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("source") Currency source, @QueryParam("destination") Currency destination,  @QueryParam("tag") Long tag, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        apiKeyService.validateKey(apiKey);

        String csv = CsvHelper.toCsv(exchangeToExchangePaymentService.search(from, to, source, destination, tag, size == null ? 300 : size, page == null ? 1 : page).getPayments());
        Response.ResponseBuilder response = Response.ok(csv);
        response.header("Content-Disposition",
                "attachment; filename=odl_" + new Date().getTime() + ".xls");
        return response.build();
    }

    @POST
    @Consumes("application/json")
    public void push(@QueryParam("key") String key, ExchangeToExchangePayment payment) {
        if (proxy) {
            log.info("Notif : ", payment);
            apiKeyService.validateMasterKey(key);
            messagingTemplate.convertAndSend("/top/odl", payment);
        }
    }

}
