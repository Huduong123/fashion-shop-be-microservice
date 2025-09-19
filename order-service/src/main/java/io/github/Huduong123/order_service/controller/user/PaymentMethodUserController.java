package io.github.Huduong123.order_service.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.order_service.dto.user.payment_method.PaymentMethodDTO;
import io.github.Huduong123.order_service.service.user.UserPaymentMethodService;

@RestController
@RequestMapping("/api/v1/order/payment-methods")
public class PaymentMethodUserController {
    private final UserPaymentMethodService userPaymentMethodService;

    public PaymentMethodUserController(UserPaymentMethodService userPaymentMethodService) {
        this.userPaymentMethodService = userPaymentMethodService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getActivePaymentMethods() {
        List<PaymentMethodDTO> paymentMethodDTOs = userPaymentMethodService.getActivePaymentMethods();
        return ResponseEntity.ok(paymentMethodDTOs);
    }

}
