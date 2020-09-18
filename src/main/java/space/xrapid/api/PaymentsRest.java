package space.xrapid.api;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Currency;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.GlobalStats;
import space.xrapid.domain.OdlPaymentsResponse;
import space.xrapid.domain.Stats;
import space.xrapid.exception.UnauthorizedException;
import space.xrapid.service.ApiKeyService;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.util.CsvHelper;

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
  public List<ExchangeToExchangePayment> getPayments(@QueryParam("key") String apiKey,
      @QueryParam("from") Long from, @QueryParam("to") Long to) {
    return exchangeToExchangePaymentService.getLasts();
  }

  @GET
  @Produces("application/json")
  @Path("/search")
  public OdlPaymentsResponse search(@QueryParam("key") String apiKey,
      @QueryParam("from") String from, @QueryParam("to") String to,
      @QueryParam("source") Currency source, @QueryParam("destination") Currency destination,
      @QueryParam("tag") Long tag, @QueryParam("page") Integer page,
      @QueryParam("size") Integer size, @Context HttpServletRequest request) {
    if (apiKey == null) {
      throw new UnauthorizedException();
    }
    apiKeyService.validateKey(apiKey, getIp(request));
    return exchangeToExchangePaymentService
        .search(from, to, source, destination, tag, size == null ? 300 : size,
            page == null ? 1 : page);
  }

  @GET
  @Produces("application/json")
  @Path("/stats")
  public Stats getStats(@QueryParam("key") String apiKey, @QueryParam("size") Integer size,
      @Context HttpServletRequest request) {

    if (size != null) {
      apiKeyService.validateKey(apiKey, getIp(request));
    }

    if (size == null) {
      size = 21;
    }
    return exchangeToExchangePaymentService.calculateStats(size);
  }

  @GET
  @Produces("application/json")
  @Path("/stats/all")
  public GlobalStats getAllStats(@QueryParam("key") String apiKey,
      @Context HttpServletRequest request) {
    if (apiKey == null) {
      throw new UnauthorizedException();
    }
    apiKeyService.validateKey(apiKey, getIp(request));
    return exchangeToExchangePaymentService.calculateGlobalStats(false);
  }

  @GET
  @Path("/search/csv")
  @Produces("text/csv")
  public Response csv(@QueryParam("key") String apiKey, @QueryParam("from") String from,
      @QueryParam("to") String to, @QueryParam("source") Currency source,
      @QueryParam("destination") Currency destination, @QueryParam("tag") Long tag,
      @QueryParam("page") Integer page, @QueryParam("size") Integer size,
      @Context HttpServletRequest request) {
    apiKeyService.validateKey(apiKey, getIp(request));

    String csv = CsvHelper.toCsv(exchangeToExchangePaymentService
        .search(from, to, source, destination, tag, size == null ? 300 : size,
            page == null ? 1 : page).getPayments());
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

  private String getIp(HttpServletRequest request) {
    String remoteAddr = request.getRemoteAddr();

    String proxyOrigin;

    if ((proxyOrigin = request.getHeader("x-forwarded-for")) != null) {
      remoteAddr = proxyOrigin;
      int idx = remoteAddr.indexOf(',');
      if (idx > -1) {
        remoteAddr = remoteAddr.substring(0, idx);
      }
    }
    return remoteAddr;
  }

}
