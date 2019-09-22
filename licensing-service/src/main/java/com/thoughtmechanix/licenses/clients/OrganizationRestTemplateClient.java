package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents the mechanism of using a
 * Ribbon-backed RestTemplate to call a service.
 *
 * With this mechanism, rather than using the physical location of the
 * service in the RestTemplate call, we're going to build the target URI
 * using the Eureka service ID of the service you want to call.
 *
 * The server name in the URL matches the application ID of the organizationservice
 * key that was used to registered the organization service in Eureka.
 *
 * The Ribbon-enabled RestTemplate will parse the URL passed into it and use
 * whatever is passed in as the server name as the key to query Ribbon for an instance
 * of a service. The actual service location and port are completely abstracted from
 * the developer.
 * In addition, by using RestTemplate class, Ribbon will round-robin load balance all
 * request among all the service instances.
 *
 * @author Ernesto A. Rodriguez Acosta
 */
@Component
public class OrganizationRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){

        // When using a Ribbon-back RestTemplate, the URL target
        // is builded with the Eureka service ID
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://organizationservice/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
