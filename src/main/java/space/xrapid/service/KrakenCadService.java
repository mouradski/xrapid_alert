package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class KrakenCadService extends KrakenUsdService {

  protected String getPair() {
    return "XRPCAD";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.KRAKEN_CAD;
  }
}
