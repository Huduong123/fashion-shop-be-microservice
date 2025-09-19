package io.github.Huduong123.order_service.mapper;

import io.github.Huduong123.order_service.entity.PaymentMethod;

import org.springframework.stereotype.Component;

import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodCreateAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodUpdateAdminDTO;
import io.github.Huduong123.order_service.dto.user.payment_method.PaymentMethodDTO;

@Component
public class PaymentMethodMapper {

    public PaymentMethodAdminDTO toPaymentMethodAdminDTO(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return PaymentMethodAdminDTO.builder()
                .id(paymentMethod.getId())
                .code(paymentMethod.getCode())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .imageUrl(paymentMethod.getImageUrl())
                .type(paymentMethod.getType())
                .enabled(paymentMethod.isEnabled())
                .createdAt(paymentMethod.getCreatedAt())
                .updatedAt(paymentMethod.getUpdatedAt())
                .build();
    }

    public PaymentMethodDTO toPaymentMethodDTO(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return PaymentMethodDTO.builder()
                .id(paymentMethod.getId())
                .code(paymentMethod.getCode())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .imageUrl(paymentMethod.getImageUrl())
                .type(paymentMethod.getType())
                .build();
    }

    public PaymentMethod convertCreateDTOToEntity(PaymentMethodCreateAdminDTO createDTO) {
        if (createDTO == null) {
            return null;
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCode(createDTO.getCode());
        paymentMethod.setName(createDTO.getName());
        paymentMethod.setDescription(createDTO.getDescription());
        paymentMethod.setImageUrl(createDTO.getImageUrl());
        paymentMethod.setType(createDTO.getType());
        paymentMethod.setEnabled(createDTO.isEnabled());
        return paymentMethod;
    }

    public void convertUpdateDTOToEntity(PaymentMethodUpdateAdminDTO updateDTO, PaymentMethod existingPaymentMethod) {
        if (updateDTO == null || existingPaymentMethod == null) {
            return;
        }

        existingPaymentMethod.setName(updateDTO.getName());
        existingPaymentMethod.setDescription(updateDTO.getDescription());
        existingPaymentMethod.setImageUrl(updateDTO.getImageUrl());
        existingPaymentMethod.setType(updateDTO.getType());
        existingPaymentMethod.setEnabled(updateDTO.getEnabled());
    }

}