package space.xrapid.service.exchange.coinfield;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class CoinfieldEurService extends CoinfieldUsdService {

  @Override
  protected String getMarket() {
    return "xrpeur";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.COINFIELD_EUR;
  }

}
