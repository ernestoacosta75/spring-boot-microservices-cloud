package com.thoughtmechanix.licenses.services.impl;


import com.thoughtmechanix.licenses.services.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List getEurekaServices(){
       List<String> services = new ArrayList<String>();

        discoveryClient.getServices().forEach(serviceName -> {
            discoveryClient.getInstances(serviceName).forEach(instance->{
                services.add( String.format("%s:%s",serviceName,instance.getUri()));
            });
        });

        return services;
    }



}
