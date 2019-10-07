package com.thoughtmechanix.licenses.services.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import com.thoughtmechanix.licenses.services.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Hystrix and Sprig Cloud use  the<b>@HystrixCommand</b> annotation
 * to mark Java class methods as being managed by a Hystrix circuit breaker.
 *
 * When the Spring framework sees this annotation, it will dinamically
 * generate a proxy that will wrapper the method and manage all calls to that
 * method through a thread pool of threads specifically set aside to handle
 * remote calls.
 *
 * @author Ernesto A. Rodriguez Acosta
 */
@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationRestTemplateClient organizationRestTemplateClient;

    @Override
    public License getLicense(String organizationId,String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = getOrganization(organizationId);

        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }

    @HystrixCommand(//fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize",value="30"),
                            @HystrixProperty(name="maxQueueSize", value="10")},
            commandProperties={
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
                    @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}
    )
    @Override
    public List<License> getLicensesByOrg(String organizationId){
        //logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();

        return licenseRepository.findByOrganizationId( organizationId );
    }

    @Override
    public License getLicensesWithClient(String organizationId, String licenseId, String clientType) {
        return null;
    }

    @Override
    public void saveLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());

        licenseRepository.save(license);
    }

    @Override
    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    @Override
    public void deleteLicense(License license){
        licenseRepository.deleteById( license.getLicenseId());
    }

    @HystrixCommand
    private Organization getOrganization(String organizationId) {
        return organizationRestTemplateClient.getOrganization(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }

    /**
     * This method gives a one in three
     * chance of a database call running long.
     */
    private void randomlyRunLong() {
        Random random = new Random();

        int randomNum = random.nextInt((3 - 1) + 1) * 1;

        if(randomNum == 3) {
           sleep();
        }
    }

    /**
     * To sleep for 11000 milliseconds (11 seconds).
     * Default Hystrix behavior is to time a call after
     * 1 second.
     */
    private void sleep() {
        try {
            Thread.sleep(11000);
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
