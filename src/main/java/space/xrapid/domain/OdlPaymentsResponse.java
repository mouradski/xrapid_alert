package space.xrapid.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({ "currentPage", "pageSize", "total", "pages", "payments" })
public class OdlPaymentsResponse {

    List<ExchangeToExchangePayment> payments;
    private int currentPage;
    private int pageSize;
    private Long total;
    private int pages;
}
