package io.github.Huduong123.gateway_service.config;



import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Sử dụng EnvironmentPostProcessor để tải các biến từ tệp .env.
 * Đây là cách được khuyến nghị để thêm các nguồn thuộc tính tùy chỉnh một cách lập trình.
 * Nó chạy rất sớm trong quá trình khởi động, đảm bảo các thuộc tính có sẵn
 * để giải quyết các placeholder trong application.yml.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        // Tải tệp .env
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Tạo một đối tượng Properties từ các mục trong Dotenv
        Properties dotenvProperties = new Properties();
        dotenv.entries().forEach(entry -> {
            dotenvProperties.setProperty(entry.getKey(), entry.getValue());
        });

        // Tạo một PropertySource từ các thuộc tính đã tải
        PropertiesPropertySource dotenvPropertySource = new PropertiesPropertySource("dotenv", dotenvProperties);

        // Thêm nguồn thuộc tính của chúng ta vào môi trường Spring
        environment.getPropertySources().addFirst(dotenvPropertySource);
    }
}