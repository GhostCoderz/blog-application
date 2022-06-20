package com.ghostcoderz.blog_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    private final String AUTHORIZATION_HEADER = "Authorization";

    private ApiKey apiKeys(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private List<SecurityContext> securityContexts(){
        SecurityContext context = SecurityContext.builder()
                .securityReferences(securityReferences())
                .build();
        return List.of(context);
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global",
                "access everything"
        );
        return List.of(new SecurityReference(
                "JWT",
                new AuthorizationScope[]{ authorizationScope }));
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .securityContexts(securityContexts())
                .securitySchemes(List.of(apiKeys()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {

        return new ApiInfo(
                "Blogging Application API",
                "This project is developed by Ved Asole(GhostCoderz)",
                "1.0",
                "Terms of Service",
                new Contact(
                    "Ved Asole",
                        "http://localhost:8080",
                        "codersarena.code@gmail.com"
                ),
                "License of APIs",
                "License Url",
                Collections.emptyList()
        );

    }

}
