package com.removewatermark.rmmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RmmarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmmarkApplication.class, args);
    }

}
