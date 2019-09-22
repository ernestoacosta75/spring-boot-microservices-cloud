package com.thoughtmechanix.licenses.services.impl;

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
import java.util.UUID;

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

    @Override
    public List<License> getLicensesByOrg(String organizationId){
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
}
