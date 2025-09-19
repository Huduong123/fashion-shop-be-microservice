package io.github.Huduong123.order_service.service.common;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImp implements FileUploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // Tạo một tên file duy nhất để tránh trùng lặp
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Chuẩn bị request để upload lên S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName) // Tên file trên S3
                .contentType(file.getContentType())
                .build();

        // Thực hiện upload
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Trả về URL công khai của file trên S3
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            // Trích xuất tên file (key) từ URL
            String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (Exception e) {
            // Ghi log lỗi
            e.printStackTrace();
            return false;
        }
    }
}