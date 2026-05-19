package ru.alafonin4.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServerApplication {
    /**
     * Starts the Spring Boot application.
     * @param args application startup arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
