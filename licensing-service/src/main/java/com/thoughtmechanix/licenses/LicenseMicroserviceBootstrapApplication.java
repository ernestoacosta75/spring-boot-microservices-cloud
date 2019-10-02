package com.thoughtmechanix.licenses;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * This class represents the bootstrap class
 * of the License service.
 * To begin using the DiscoveryClient the
 * <b>@EnableDiscoveryClient</b> annotations is mandatory.
 *
 * Instead, invoking services with Ribbon-aware Spring Rest Template
 * is one of the more common mechanisms for interacting with Ribbon
 * via Spring. To use a Ribbon-aware <b>RestTemplate</b> class, its
 * have to be defined a RestTemplate bean construction method with
 * a Spring Cloud annotation called <b>@LoadBalanced</b>.
 * This annotation tells Spring Cloud to create a Ribbon backed
 * RestTemplate class.In that way, any time we want to use the
 * RestTemplate bean to call a service, we only need to auto-wire it
 * into the class using it.
 *
 * @author Ernesto A. Rodriguez Acosta
 */
@SpringBootApplication
@RefreshScope
@EnableEurekaClient
@EnableCircuitBreaker
public class LicenseMicroserviceBootstrapApplication {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicenseMicroserviceBootstrapApplication.class, args);
    }
}
