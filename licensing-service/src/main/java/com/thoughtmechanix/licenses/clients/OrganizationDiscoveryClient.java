package com.thoughtmechanix.licenses.clients;

import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * This class represents the Spring DiscoveryClient.
 * Its offers the lowest level of access to Ribbon and
 * the services registered within it. Using the DiscoveryClient,
 * it's posible to query for all the services registered with
 * the Ribbon client and their corresponding URLs and then call
 * the service using a standard RestTemplate class.
 *
 * @author Ernesto A. Rodriguez Acosta
 */
@Component
public class OrganizationDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;    // DiscoveryClient is auto-injected into the class

    public Organization getOrganization(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();

        // Gets a list of all the instances of organization services
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

        if(instances.size() == 0) return null;

        // Retrieves the service endpoint to call
        String serviceUri = String.format("%s/v1/organizations/%s",
                                    instances.get(0).getUri().toString(), organizationId);

        // Uses a standard Spring REST Template class to call the service
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();

    }
}
