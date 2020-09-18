package space.xrapid.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Provider
@Slf4j
public class ExceptionHandler implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException ex) {

    if (UnauthorizedException.class.isInstance(ex) || MomentarilyBlockedException.class
        .isInstance(ex)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
    } else {
      log.error("Internal Error", ex);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Error")
          .build();
    }
  }
}
