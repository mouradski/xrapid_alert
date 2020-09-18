package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class KrakenEurService extends KrakenUsdService {

  protected String getPair() {
    return "XRPEUR";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.KRAKEN_EUR;
  }
}
