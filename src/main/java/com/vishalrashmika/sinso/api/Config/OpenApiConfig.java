package com.vishalrashmika.sinso.api.Config;

import org.springframework.beans.factory.annotation.Value;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Vishal Rashmika",
            email = "vishal@vishalrashmika.com",
            url = "https://vishalrashmika.com/"
        ),
        description = "OpenApi documentation for SinSo API - Largest Open-Source Sinhala Songs Lyrics API",
        title = "SinSo API Documentation",
        version = "${spring.application.version:1.0.0}",
        license = @License(
            name = "GNU General Public License v3.0",
            url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
        )
    ),
    servers = {
        @Server(
            description = "Local Development Environment",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Production Environment",
            url = "https://api.sinso.vishalrashmika.com"
        )
    }
)
public class OpenApiConfig {
    @Value("${spring.application.version:1.0.0}")
    private String applicationVersion;
}