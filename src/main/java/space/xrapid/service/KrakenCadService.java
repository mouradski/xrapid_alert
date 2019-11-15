package space.xrapid.service;

import org.springframework.stereotype.Service;

@Service
public class KrakenCadService extends KrakenUsdService {
    protected String getPair() {
        return "XRPCAD";
    }
}
