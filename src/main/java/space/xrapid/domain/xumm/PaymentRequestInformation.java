package space.xrapid.domain.xumm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequestInformation {

  private String paymentId;
  private String qrCodeUrl;
}
