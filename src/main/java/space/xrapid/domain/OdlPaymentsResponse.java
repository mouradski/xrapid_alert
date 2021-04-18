package space.xrapid.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OdlPaymentsResponse {

    List<ExchangeToExchangePayment> payments;
    private int currentPage;
    private int pageSize;
    private Long total;
    private int pages;
}
