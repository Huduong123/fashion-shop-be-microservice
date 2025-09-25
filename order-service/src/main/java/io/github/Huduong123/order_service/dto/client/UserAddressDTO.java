package io.github.Huduong123.order_service.dto.client;

import lombok.Data;

@Data
public class UserAddressDTO {
    private String recipientName;
    private String phoneNumber;
    private String addressDetail;
}