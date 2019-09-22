package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.services.LicenseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Get a list of licenses", response= List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value="/")
    public List<License> getLicenses( @PathVariable("organizationId") String organizationId) {

        return licenseService.getLicensesByOrg(organizationId);
    }

    @ApiOperation(value = "Get an specific license based in its ID", response= License.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved license"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value="/{licenseId}")
    public License getLicenses( @ApiParam(value = "The organization id", required = true)
                                @PathVariable("organizationId") String organizationId,
                                @ApiParam(value = "The license id", required = true)
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
    @ApiOperation(value = "Get an specific license using an specific discovery client", response= License.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved license"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value="/{licenseId}/{clientType}")
    public String getLicensesWithClient(@ApiParam(value = "The organization id", required = true)
                                        @PathVariable("organizationId") String organizationId,
                                        @ApiParam(value = "The license id", required = true)
                                        @PathVariable("licenseId") String licenseId,
                                        @ApiParam(value = "The client type", required = true)
                                        @PathVariable("clientType") String clientType) {
        return String.format("This is the Delete");
    }

    @ApiOperation(value = "Update a license", response= String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated license"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping(value="/{licenseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String updateLicense( @ApiParam(value = "The license id", required = true)
                                  @PathVariable("licenseId") String licenseId) {
        return String.format("This is the put");
    }

    @ApiOperation(value = "Create a license")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved license"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping(value="/")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLicense(@RequestBody License license) {
       licenseService.saveLicense(license);
    }

    @ApiOperation(value = "Delete a license", response= String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted license"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping(value="/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicense( @ApiParam(value = "The license id", required = true)
                                 @PathVariable("licenseId") String licenseId) {
        return String.format("This is the Delete");
    }
}
