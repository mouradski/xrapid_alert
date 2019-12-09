package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.XrapidInboundAddress;
import space.xrapid.repository.XrapidInboundAddressRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
public class XrapidInboundAddressService {

    @Autowired
    private XrapidInboundAddressRepository xrapidInboundAddressRepository;

    @Transactional
    public void add(ExchangeToExchangePayment payment) {

        XrapidInboundAddress inboundXrapidCorridors = xrapidInboundAddressRepository.getByAddressAndTagAndSourceFiat(payment.getDestinationAddress(), payment.getTag(), payment.getSourceFiat());

        if (inboundXrapidCorridors == null) {
            inboundXrapidCorridors = XrapidInboundAddress.builder().address(payment.getDestinationAddress()).tag(payment.getTag()).sourceFiat(payment.getSourceFiat()).recurrence(1).build();
        } else {
            inboundXrapidCorridors.setRecurrence(inboundXrapidCorridors.getRecurrence() + 1);
        }

        xrapidInboundAddressRepository.save(inboundXrapidCorridors);
    }

    public boolean isXrapidDestination(ExchangeToExchangePayment payment) {
        boolean result =  xrapidInboundAddressRepository.existsByAddressAndTagAndSourceFiatAndRecurrenceGreaterThan(payment.getDestinationAddress(), payment.getTag(), payment.getSourceFiat(), 100);
        if (result) {
            log.info("{}:{} is an ODL confirmed destination.", payment.getDestinationAddress(), payment.getTag());
        }
        return result;
    }
}
