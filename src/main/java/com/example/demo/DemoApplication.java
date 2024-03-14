package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            String secretKey = dotenv.get("JWT_SECRET_KEY", "defaultSecret");
            System.setProperty("JWT_SECRET_KEY", secretKey); 
        } catch (Exception e) {
            // .env 文件可能在生产环境中不存在，所以忽略异常
            // 这里可以记录日志或者执行其他回退逻辑
			// 生产环境启动 docker run -e JWT_SECRET_KEY=prodSecretKey myapp:latest
            System.out.println("No .env file found or error loading it, falling back to system environment variables.");
        }

		SpringApplication.run(DemoApplication.class, args);
	}

}


