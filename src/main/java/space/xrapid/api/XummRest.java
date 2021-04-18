package space.xrapid.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.ApiKey;
import space.xrapid.domain.xumm.PaymentRequestInformation;
import space.xrapid.domain.xumm.webhook.WebHook;
import space.xrapid.service.RateService;
import space.xrapid.service.XummService;

import javax.ws.rs.*;

@Path("/xumm")
@Slf4j
public class XummRest {

    @Autowired
    private XummService xummService;

    @Autowired
    private RateService rateService;


    @GET
    @Produces("application/json")
    public PaymentRequestInformation requestPayment(@QueryParam("days") Long days,
                                                    @QueryParam("key") String key) {

        if (days == null) {
            days = 1l;
        }

        if (days < 1) {
            days = 1l;
        }

        double price = 1;

        if (days >= 180) {
            price = 0.6 * price;
        } else if (days >= 60) {
            price = 0.8 * price;
        }

        double rate = rateService.getXrpUsdRate();

        return xummService.requestPayment(Math.ceil((price * days) / rate), "XRP", days, key);
    }

    @GET
    @Path("/{id}")
    public ApiKey getApiKey(@PathParam("id") String id) {
        String status = xummService.verifyPayment(id);
        if ("SIGNED".equals(status)) {

            ApiKey apiKey = xummService.getRenewedApiKey(id);

            if (apiKey != null) {
                return apiKey;
            }

            return xummService.getKey(id);
        } else {
            return ApiKey.builder().key(status).build();
        }
    }

    @POST
    @Consumes({"application/json"})
    @Path("/webhooks")
    public void notif(WebHook webHook) {
        try {
            log.info(new ObjectMapper().writeValueAsString(webHook));
        } catch (Exception e) {
        }

        xummService.updatePaymentStatus(webHook);
    }
}
