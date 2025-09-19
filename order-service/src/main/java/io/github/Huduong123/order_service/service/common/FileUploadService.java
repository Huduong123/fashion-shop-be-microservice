package io.github.Huduong123.order_service.service.common;


import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file) throws IOException;
    boolean deleteFile(String fileName);
} 