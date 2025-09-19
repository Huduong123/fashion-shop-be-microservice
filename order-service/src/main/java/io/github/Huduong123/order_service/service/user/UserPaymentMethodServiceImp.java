package io.github.Huduong123.order_service.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.Huduong123.order_service.dto.user.payment_method.PaymentMethodDTO;
import io.github.Huduong123.order_service.mapper.PaymentMethodMapper;
import io.github.Huduong123.order_service.repository.PaymentMethodRepository;

@Service
public class UserPaymentMethodServiceImp implements UserPaymentMethodService{
    
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    public UserPaymentMethodServiceImp(PaymentMethodMapper paymentMethodMapper, PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodMapper = paymentMethodMapper;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethodDTO> getActivePaymentMethods() {
        return paymentMethodRepository.findByIsEnabledTrue()
            .stream()
            .map(paymentMethodMapper::toPaymentMethodDTO)
            .collect(Collectors.toList());
    }

    
}
