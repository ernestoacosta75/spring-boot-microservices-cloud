package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.services.DiscoveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/tools")
@Api(value = "v1/tools")
public class ToolsController {
    @Autowired
    private DiscoveryService discoveryService;

    @ApiOperation(value = "Get a list of the Eureka services", response= List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/eureka/services")
    public List<String> getEurekaServices() {
        return discoveryService.getEurekaServices();
    }
}
