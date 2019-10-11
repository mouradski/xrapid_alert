package space.xrapid.service;

import space.xrapid.domain.ripple.Payment;

import java.util.List;

@FunctionalInterface
public interface SubmitXrpLedgerPayments {
    void submit(List<Payment> payments);
}
