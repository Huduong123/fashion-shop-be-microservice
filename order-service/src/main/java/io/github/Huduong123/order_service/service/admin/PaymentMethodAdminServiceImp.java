package io.github.Huduong123.order_service.service.admin;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sun.jdi.request.DuplicateRequestException;

import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodCreateAdminDTO;
import io.github.Huduong123.order_service.dto.admin.payment_method.PaymentMethodUpdateAdminDTO;
import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.order_service.entity.PaymentMethod;
import io.github.Huduong123.order_service.exception.DuplicateResourceException;
import io.github.Huduong123.order_service.exception.NotFoundException;
import io.github.Huduong123.order_service.mapper.PaymentMethodMapper;
import io.github.Huduong123.order_service.repository.OrderRepository;
import io.github.Huduong123.order_service.repository.PaymentMethodRepository;
import io.github.Huduong123.order_service.service.common.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodAdminServiceImp implements PaymentMethodAdminService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;
    private final PaymentMethodMapper paymentMethodMapper;
    private final FileUploadService fileUploadService;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodAdminDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(paymentMethodMapper::toPaymentMethodAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodAdminDTO getPaymentMethodById(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment method not found with ID: " + id));

        return paymentMethodMapper.toPaymentMethodAdminDTO(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodAdminDTO createPaymentMethod(PaymentMethodCreateAdminDTO createDTO, MultipartFile file)
            throws IOException {
        String normalizedCode = createDTO.getCode().toLowerCase().replaceAll("\\s+", "-");

        paymentMethodRepository.findByCode(normalizedCode).ifPresent(pm -> {
            throw new DuplicateRequestException("Code '" + createDTO.getCode() + "' already exists");
        });
        PaymentMethod newPaymentMethod = paymentMethodMapper.convertCreateDTOToEntity(createDTO);
        
        if (file != null && !file.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(file);
            newPaymentMethod.setImageUrl(imageUrl);
        }

        PaymentMethod savPaymentMethod = paymentMethodRepository.save(newPaymentMethod);
        return paymentMethodMapper.toPaymentMethodAdminDTO(savPaymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodAdminDTO updatePaymentMethod(Long id, PaymentMethodUpdateAdminDTO updateDTO, MultipartFile file)
            throws IOException {
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment method not found with ID: " + id));
    
        if (!existingPaymentMethod.getName().equals(updateDTO.getName())) {
            paymentMethodRepository.findByName(updateDTO.getName()).ifPresent(pm -> {
                throw new DuplicateResourceException(
                    "Tên phương thức thanh toán '" + updateDTO.getName() + "' đã tồn tại.");
                });
            }
            if (file != null && !file.isEmpty()) {
                // Lấy URL ảnh cũ để xóa
                String oldImageUrl = existingPaymentMethod.getImageUrl();
                
                // Upload ảnh mới
                String newImageUrl = fileUploadService.uploadFile(file);
                updateDTO.setImageUrl(newImageUrl); // Cập nhật DTO với URL mới
                
                // Xóa ảnh cũ nếu tồn tại
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    fileUploadService.deleteFile(oldImageUrl);
                }
            } else {
                // Nếu không có file mới, giữ lại ảnh cũ
                updateDTO.setImageUrl(existingPaymentMethod.getImageUrl());
            }
            // BƯỚC 3: Dùng mapper để cập nhật các trường của đối tượng đã tồn tại.
            paymentMethodMapper.convertUpdateDTOToEntity(updateDTO, existingPaymentMethod);

            PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(existingPaymentMethod);
            return paymentMethodMapper.toPaymentMethodAdminDTO(updatedPaymentMethod);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deletePaymentMethod(Long id) {
        PaymentMethod paymentMethodToDelete = paymentMethodRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Payment method not found with ID: " + id));
        
        // Thay đổi quan trọng: Kiểm tra ràng buộc dựa trên 'code'
        boolean isUsedInOrders = orderRepository.existsByPaymentMethodCode(paymentMethodToDelete.getCode());
        if (isUsedInOrders) {
            throw new IllegalStateException(
                    "Cannot delete this payment method because it is used in existing orders.");
        }
        
        // Xóa ảnh trên S3 nếu có
        String imageUrl = paymentMethodToDelete.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            fileUploadService.deleteFile(imageUrl);
        }
        
        paymentMethodRepository.delete(paymentMethodToDelete);
        return new ResponseMessageDTO(HttpStatus.OK, "Successfully deleted payment method with ID " + id);
    }

}
