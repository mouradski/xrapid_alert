package space.xrapid.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeToFiatPayment extends Payment {
    private Exchange source;
    private Currency destinationCurrency;
}
