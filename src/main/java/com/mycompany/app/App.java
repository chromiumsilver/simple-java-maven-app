package com.mycompany.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world web application!
 */
@SpringBootApplication
@RestController
public class App {

    private static final String MESSAGE = "Hello World!";

    public App() {}

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/")
    public String getMessage() {
        return MESSAGE;
    }
}
