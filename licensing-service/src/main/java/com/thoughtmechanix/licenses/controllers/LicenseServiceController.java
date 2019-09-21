package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The "/{licenseId}/{clientType}" route will allow to specify the
 * type of client is wanted to invoke the service with. This is a
 * helper route to explore each of the different methods for invoking
 * the organization service via Ribbon, through a single route.
 *
 * @author Ernesto A. Rodriguez Acosta
 */
@RestController
@RequestMapping(value="v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {
    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ServiceConfig serviceConfig;

    @GetMapping(value="/")
    public List<License> getLicenses( @PathVariable("organizationId") String organizationId) {

        return licenseService.getLicensesByOrg(organizationId);
    }

    @GetMapping(value="/{licenseId}")
    public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {

        return licenseService.getLicense(organizationId,licenseId);
    }

    /**
     *
     * @param organizationId
     * @param licenseId
     * @param clientType    It will drive the type of client to use.
     *                      The specific types to pass in on this route include:
     *                      -Discovery: Uses the discovery client and a standard
     *                                  Spring Rest Template class to invoke the
     *                                  organization service.
     *
     *                      -Rest: Uses an enhanced Spring Template to invoke the
     *                             Ribbon-based-service
     *
     *                      -Feign: Uses Netflix's Feign client library to invoke
     *                              a service via Ribbon
     * @return
     */
    @GetMapping(value="/{licenseId}/{clientType}")
    public String getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                                         @PathVariable("licenseId") String licenseId,
                                         @PathVariable("clientType") String clientType) {
        return String.format("This is the Delete");
    }

    @PutMapping(value="/{licenseId}")
    public String updateLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the put");
    }

    @PostMapping(value="/")
    public void saveLicenses(@RequestBody License license) {
       licenseService.saveLicense(license);
    }

    @DeleteMapping(value="/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the Delete");
    }
}
