package com.thoughtmechanix.licenses.services.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.thoughtmechanix.licenses.clients.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import com.thoughtmechanix.licenses.services.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestTemplateClient;

    @Override
    public License getLicense(String organizationId,String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        license.setComment(config.getExampleProperty());

        return license;
    }

    @HystrixCommand
    @Override
    public List<License> getLicensesByOrg(String organizationId){
        randomlyRunLong();

        return licenseRepository.findByOrganizationId( organizationId );
    }

    @Override
    public License getLicensesWithClient(String organizationId, String licenseId, String clientType) {
        License license = licenseRepository
                            .findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization organization = retrieveOrgInfo(organizationId, clientType);

        license.setOrganizationName(organization.getName());
        license.setContactName(organization.getContactName());
        license.setContactEmail(organization.getContactEmail());
        license.setContactPhone(organization.getContactPhone());
        license.setComment(config.getExampleProperty());

        return license;
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

    private Organization retrieveOrgInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                log.info("Using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                log.info("Using the rest client");
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                log.info("Using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestTemplateClient.getOrganization(organizationId);
        }

        return organization;
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
