package com.vishalrashmika.sinso.api.Config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "${app.contact.name:Vishal Rashmika}",
            email = "${app.contact.email:contact@vishalrashmika.com}",
            url = "${app.contact.url:https://vishalrashmika.com}"
        ),
        description = "${app.doc_description:REST API documentation for SINSO music platform}",
        title = "${app.doc_title:SINSO API Documentation}",
        version = "${app.doc_version:1.0.0}",
        license = @License(
            name = "GNU General Public License v3.0",
            url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
        )
    ),
    servers = {
        @Server(
            description = "Production Environment",
            url = "https://api.sinso.vishalrashmika.com"
        )
    }
)
public class OpenApiConfig {
    @Value("${app.doc_version:1.0.0}")
    private String docVersion;

    @Value("${app.doc_title:SINSO API Documentation}")
    private String appDocTitle;

    @Value("${app.doc_description:REST API documentation for SINSO music platform}")
    private String appDocDescription;

    @Value("${app.contact.name:Vishal Rashmika}")
    private String contactName;

    @Value("${app.contact.email:contact@vishalrashmika.com}")
    private String contactEmail;

    @Value("${app.contact.url:https://vishalrashmika.com}")
    private String contactUrl;
}