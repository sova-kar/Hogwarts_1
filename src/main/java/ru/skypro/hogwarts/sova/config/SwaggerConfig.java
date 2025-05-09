package ru.skypro.hogwarts.sova.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Hogwarts School API",
        version = "1.0",
        description = "API для управления студентами и факультетами Хогвартса",
        contact = @Contact(name = "Hogwarts Admin", email = "admin@hogwarts.ru")
))
public class SwaggerConfig {
}
