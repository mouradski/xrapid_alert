package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.ExchangeToExchangePayment;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ExchangeToExchangePaymentRepository extends JpaRepository<ExchangeToExchangePayment, String> {

    @Query(value = "SELECT * FROM exchange_payment ORDER BY date_time DESC LIMIT ?", nativeQuery = true)
    List<ExchangeToExchangePayment> findTop(int limit);

    @Query(value = "SELECT SUM(ep.amount) FROM exchange_payment ep", nativeQuery = true)
    Double getAllTimeVolume();

    @Query(value = "SELECT SUM(ep.amount) FROM exchange_payment ep WHERE ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    Double getVolumeBetween(long startTimestamp, long endTimestamp);

    @Query(value = "SELECT SUM(ep.amount) FROM exchange_payment ep WHERE ep.source = ? AND ep.destination = ? AND ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    Double getVolumeBySourceAndDestinationBetween(String source, String destination, long startTimestamp, long endTimestamp);


    @Query(value = "SELECT * FROM exchange_payment ORDER BY date_time ASC LIMIT 1", nativeQuery = true)
    ExchangeToExchangePayment getFirstOdl();

    boolean existsByTransactionHash(String transactionHash);
}
