package space.xrapid.api;

import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.DestinationTagRepeat;
import space.xrapid.service.ApiKeyService;
import space.xrapid.service.DestinationTagRepeatService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/tags")
public class DestinationTagRest {

    @Autowired
    private DestinationTagRepeatService destinationTagRepeatService;


    @Autowired
    private ApiKeyService apiKeyService;

    @GET
    @Produces("application/json")
    @Path("/")
    public List<DestinationTagRepeat> getTags(@QueryParam("key") String apiKey) {
        apiKeyService.validateKey(apiKey);

        return destinationTagRepeatService.getAll();
    }

    @GET
    @Path("/csv")
    @Produces("text/csv")
    public Response csv(@QueryParam("key") String apiKey) {
        apiKeyService.validateKey(apiKey);
        String header = "SOURCE EXCHANGE;SOURCE ADDRESS;DESTINATION EXCHANGE;DESTINATION ADDRESS;TAG;REPEATED";
        Response.ResponseBuilder response = Response
                .ok(header + "\n" + destinationTagRepeatService.getAll().stream()
                        .map(DestinationTagRepeat::toString).limit(10000).collect(Collectors.joining("\n")));
        response.header("Content-Disposition",
                "attachment; filename=export.xls");
        return response.build();
    }

}
