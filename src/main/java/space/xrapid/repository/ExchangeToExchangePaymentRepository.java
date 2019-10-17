package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.ExchangeToExchangePayment;

import java.util.List;

@Repository
public interface ExchangeToExchangePaymentRepository extends JpaRepository<ExchangeToExchangePayment, String> {

    @Query(value = "SELECT * FROM EXCHANGE_PAYMENT ORDER BY date_time DESC LIMIT ?", nativeQuery = true)
    List<ExchangeToExchangePayment> findTop(int limit);

    boolean existsByTransactionHash(String transactionHash);
}
