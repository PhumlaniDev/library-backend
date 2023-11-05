package com.phumlani_dev.library.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Phumlani",
                        email = "aphumlani.dev@gmail.com",
                        url = "https://www.aphumlani.netlify.app"
                ),
                description = "OpenApi documentation for Spring Boot",
                title = "OpenApi",
                version = "1.0",
                license = @License(
                        name = "MIT License v1.0",
                        url = "https://www.mit.license.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:7000"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "https://www.bms.com"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuTh"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
