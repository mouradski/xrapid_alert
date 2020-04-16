package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.ExchangeToExchangePayment;

import java.util.List;
import java.util.Set;

@Repository
public interface ExchangeToExchangePaymentRepository extends CrudRepository<ExchangeToExchangePayment, Integer>, JpaSpecificationExecutor<ExchangeToExchangePayment> {

    @Query(value = "SELECT * FROM exchange_payment ORDER BY date_time DESC LIMIT ?", nativeQuery = true)
    List<ExchangeToExchangePayment> findTop(int limit);

    @Query(value = "SELECT * FROM exchange_payment WHERE in_trade_found IS TRUE OR out_trade_found IS TRUE ORDER BY date_time DESC LIMIT ?", nativeQuery = true)
    List<ExchangeToExchangePayment> findTopWithTrades(int limit);

    @Query(value = "SELECT SUM(ep.usd_value) FROM exchange_payment ep", nativeQuery = true)
    Double getAllTimeVolume();

    @Query(value = "SELECT SUM(ep.usd_value) FROM exchange_payment ep WHERE ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    Double getVolumeBetween(long startTimestamp, long endTimestamp);

    @Query(value = "SELECT SUM(ep.usd_value) FROM exchange_payment ep WHERE ep.source = ? and ep.destination = ? & ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    Double getVolumeBetween(String source, String destination, long startTimestamp, long endTimestamp);

    @Query(value = "SELECT * FROM exchange_payment ep WHERE ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    List<ExchangeToExchangePayment> findByDate(long startTimestamp, long endTimestamp);

    @Query(value = "SELECT SUM(ep.usd_value) FROM exchange_payment ep WHERE ep.source = ? AND ep.destination = ? AND ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    Double getVolumeBySourceAndDestinationBetween(String source, String destination, long startTimestamp, long endTimestamp);

    @Query(value = "SELECT SUM(ep.usd_value) FROM exchange_payment ep WHERE ep.source_fiat = ? AND ep.destination_fiat = ? AND ep.timestamp >= ? AND ep.timestamp <= ?", nativeQuery = true)
    Double getVolumeBySourceFiatAndDestinationFiatBetween(String source, String destination, long startTimestamp, long endTimestamp);

    @Query(value = "SELECT * FROM exchange_payment ORDER BY date_time ASC LIMIT 1", nativeQuery = true)
    ExchangeToExchangePayment getFirstOdl();

    @Query(value = "SELECT DISTINCT ep.destination_fiat FROM exchange_payment ep", nativeQuery = true)
    Set<String> getAllDestinationCurrencies();

    @Query(value = "SELECT DISTINCT ep.source_fiat FROM exchange_payment ep", nativeQuery = true)
    Set<String> getAllSourceCurrencies();

    ExchangeToExchangePayment findTopByOrderByTimestampAsc();

    boolean existsByTransactionHash(String transactionHash);

    ExchangeToExchangePayment getByTransactionHash(String transactionHash);
}
