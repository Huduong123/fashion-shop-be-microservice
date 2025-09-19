package io.github.Huduong123.order_service.controller.admin;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodCreateAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodUpdateAdminDTO;
import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.order_service.service.admin.PaymentMethodAdminService;
@RestController
@RequestMapping("/api/v1/order/admin/payment-methods")
public class PaymentMethodAdminController {
    private final PaymentMethodAdminService paymentMethodAdminService;

    public PaymentMethodAdminController(PaymentMethodAdminService paymentMethodAdminService) {
        this.paymentMethodAdminService = paymentMethodAdminService;
    }

    
    @GetMapping
    public ResponseEntity<List<PaymentMethodAdminDTO>> getAllPaymentMethods() {
        List<PaymentMethodAdminDTO> paymentMethods = paymentMethodAdminService.getAllPaymentMethods();
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodAdminDTO> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodAdminDTO paymentMethod = paymentMethodAdminService.getPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PaymentMethodAdminDTO> createPaymentMethod(
            @RequestPart("data") @Valid PaymentMethodCreateAdminDTO createDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        PaymentMethodAdminDTO newPaymentMethod = paymentMethodAdminService.createPaymentMethod(createDTO, file);
        return new ResponseEntity<>(newPaymentMethod, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<PaymentMethodAdminDTO> updatePaymentMethod(
            @PathVariable Long id,
            @RequestPart("data") @Valid PaymentMethodUpdateAdminDTO updateDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        PaymentMethodAdminDTO updatedPaymentMethod = paymentMethodAdminService.updatePaymentMethod(id, updateDTO, file);
        return ResponseEntity.ok(updatedPaymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> deletePaymentMethod(@PathVariable Long id) {
        ResponseMessageDTO responseMessage = paymentMethodAdminService.deletePaymentMethod(id);
        return ResponseEntity.ok(responseMessage);
    }
}
