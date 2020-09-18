package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class CoinfieldJpyService extends CoinfieldUsdService {

  @Override
  protected String getMarket() {
    return "xrpjpy";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.COINFIELD_JPY;
  }
}
