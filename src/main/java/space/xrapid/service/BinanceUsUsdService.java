package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class BinanceUsUsdService extends BinanceRubService {

  @Override
  protected String getApiUrl() {
    return "https://api.binance.us/api/v3/trades?symbol=XRPUSD&limit=1000";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.BINANCE_US;
  }
}
