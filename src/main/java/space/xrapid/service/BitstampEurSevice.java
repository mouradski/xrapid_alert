package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class BitstampEurSevice extends BitstampUsdService {

  public BitstampEurSevice() {
    this.apiUrl = "https://www.bitstamp.net/api/v2/transactions/xrpeur";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.BITSTAMP_EUR;
  }
}
