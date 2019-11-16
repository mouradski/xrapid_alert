package space.xrapid.conf;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class AppContainerCustomizer 
            implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
   
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(8080);
    }
}
