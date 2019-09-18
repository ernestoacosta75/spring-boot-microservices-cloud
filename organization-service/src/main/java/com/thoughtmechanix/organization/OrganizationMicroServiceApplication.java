package com.thoughtmechanix.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OrganizationMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationMicroServiceApplication.class, args);
    }
}
