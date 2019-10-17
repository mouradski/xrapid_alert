package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.repository.ExchangeToExchangePaymentRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExchangeToExchangePaymentService {

    @Autowired
    private ExchangeToExchangePaymentRepository repository;

    @Transactional
    public boolean save(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (repository.existsByTransactionHash(exchangeToExchangePayment.getTransactionHash())) {
            return false;
        }

        repository.save(exchangeToExchangePayment);

        return true;
    }

    public List<ExchangeToExchangePayment> getLasts() {
        return repository.findTop(20);
    }
}
