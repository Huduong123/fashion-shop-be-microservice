package io.github.Huduong123.order_service.service.user;

import java.util.List;

import io.github.Huduong123.order_service.dto.user.payment_method.PaymentMethodDTO;


public interface UserPaymentMethodService {
    List<PaymentMethodDTO> getActivePaymentMethods();
}
