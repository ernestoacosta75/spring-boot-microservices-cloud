package com.thoughtmechanix.licenses;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * This class represents the bootstrap class
 * of the License service.
 * To begin using the DiscoveryClient the
 * <b>@EnableDiscoveryClient</b> annotations is mandatory.
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
//@EnableFeignClients
public class Application {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
