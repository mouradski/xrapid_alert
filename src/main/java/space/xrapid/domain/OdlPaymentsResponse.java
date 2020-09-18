package space.xrapid.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
