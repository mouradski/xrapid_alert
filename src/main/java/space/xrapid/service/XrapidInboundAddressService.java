package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.XrapidInboundAddress;
import space.xrapid.repository.XrapidInboundAddressRepository;

@Service
@Transactional(readOnly = true)
public class XrapidInboundAddressService {

    @Autowired
    private XrapidInboundAddressRepository xrapidInboundAddressRepository;

    @Transactional
    public void add(XrapidInboundAddress xrapidInboundAddress) {
        if (!isXrapidInbound(xrapidInboundAddress.getAddress(), xrapidInboundAddress.getTag())) {
            xrapidInboundAddressRepository.save(xrapidInboundAddress);
        }
    }

    @Transactional
    public void add(ExchangeToExchangePayment payment) {
        if (!isXrapidInbound(payment.getSourceAddress(), payment.getTag())) {
            add(payment.getSourceAddress(), payment.getTag());
        }
    }

    @Transactional
    public void add(String address, Long tag) {
        add(XrapidInboundAddress.builder().address(address).tag(tag).build());
    }

    public boolean isXrapidInbound(String address, Long tag) {
        return xrapidInboundAddressRepository.existsByAddressAndTag(address, tag);
    }
}
