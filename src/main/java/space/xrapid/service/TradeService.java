package space.xrapid.service;

import space.xrapid.domain.XrpTrade;

import java.time.OffsetDateTime;
import java.util.List;

public interface TradeService {
    List<XrpTrade> fetchTrades(OffsetDateTime begin);
}
