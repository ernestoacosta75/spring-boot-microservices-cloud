package com.thoughtmechanix.licenses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class is used for the Swagger 2 configuration.
 * A  Springfox  Docket instance provides the primary API configuration
 * with sensible defaults and convenience methods for configuration.
 *
 * In this configuration class, the  @EnableSwagger2  annotation enables Swagger
 * support in the class. The  select()  method called on the Docket bean instance
 * returns an  ApiSelectorBuilder , which provides the  apis()  and  paths()  methods
 * that are used to filter the controllers and methods that are being documented using String predicates.
 *
 * The Swagger UI is available at http://localhost:8080/swagger-ui.html
 *
 * @author Ernesto Antonio Rodriguez Acosta
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.bolsadeideas.springboot.backend.apirest.controllers"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Spring Boot & Angular Integration REST API")
                .contact(new Contact("Ernesto Antonio Rodriguez Acosta", "", "ernestoacostacuba@yahoo.com.mx"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
