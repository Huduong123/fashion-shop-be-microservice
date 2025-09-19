package io.github.Huduong123.order_service.service.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodCreateAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodUpdateAdminDTO;
import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;

public interface PaymentMethodAdminService {
      /**
     * Lấy tất cả các phương thức thanh toán.
     */
    List<PaymentMethodAdminDTO> getAllPaymentMethods();

    /**
     * Lấy một phương thức thanh toán theo ID.
     */
    PaymentMethodAdminDTO getPaymentMethodById(Long id);

    /**
     * Tạo một phương thức thanh toán mới.
     * @param createDTO Dữ liệu để tạo.
     * @param file Ảnh đại diện (tùy chọn).
     * @return DTO của phương thức vừa được tạo.
     */
    PaymentMethodAdminDTO createPaymentMethod(PaymentMethodCreateAdminDTO createDTO, MultipartFile file) throws IOException;

    /**
     * Cập nhật một phương thức thanh toán đã có.
     * @param id ID của phương thức cần cập nhật.
     * @param updateDTO Dữ liệu để cập nhật.
     * @param file Ảnh đại diện mới (tùy chọn).
     * @return DTO của phương thức vừa được cập nhật.
     */
    PaymentMethodAdminDTO updatePaymentMethod(Long id, PaymentMethodUpdateAdminDTO updateDTO, MultipartFile file) throws IOException;

    /**
     * Xóa một phương thức thanh toán.
     * Sẽ thất bại nếu phương thức này đã được sử dụng trong bất kỳ đơn hàng nào.
     */
    ResponseMessageDTO deletePaymentMethod(Long id);
}
