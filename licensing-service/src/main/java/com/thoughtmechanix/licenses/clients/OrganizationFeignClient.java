package com.thoughtmechanix.licenses.clients;


import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This interface represents the mechanism to invoke services
 * with Netflix Feign client.
 *
 * The Feign library takes a different approach to calling a
 * REST service by having the developer first define a Java interface
 * and then annotating that interface with Spring Cloud annotations to
 * map what Eureka-based service Ribbon will invoke.
 * The Spring Cloud framework will dynamically generate a proxy class
 * that will be used to invoke the targeted REST service.
 * There’s no code being written for calling the service other than an
 * interface definition.
 *
 * The <b>@FeignClient</b> annotation identify the service to Feign.
 *
 * The path and action to the endpoint desired os defined using the
 * <b>@GetMapping</b> annotation. The parameters passed into the endpoint
 * are defined using the <b>@PatVariable</b> annotation.
 *
 * @author Ernesto A. Rodriguez Acosta
 */
@FeignClient("organizationservice")
public interface OrganizationFeignClient {

    @GetMapping(value = "/v1/organizations/{organizationId}",
                        consumes = "application/json")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
