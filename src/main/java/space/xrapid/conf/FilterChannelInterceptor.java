package space.xrapid.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import space.xrapid.service.ApiKeyService;

@Component
public class FilterChannelInterceptor extends ChannelInterceptorAdapter {

  @Autowired
  private ApiKeyService apiKeyService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
    if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) && headerAccessor
        .getDestination().contains("/top/odl")) {
      if (headerAccessor.getNativeHeader("apiKey") != null) {
        String apiKey = headerAccessor.getNativeHeader("apiKey").get(0);
        try {
          apiKeyService.validateKey(apiKey);
        } catch (Throwable e) {
          System.out.println("return null;");
          return null;
        }
      } else {
        System.out.println("return null;");
        return null;
      }
    }

    return message;
  }
}
