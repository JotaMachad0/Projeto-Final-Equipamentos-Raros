package br.com.raroacademy.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Equipamentos Raros API",
                version = "1.0",
                description = "API para gerenciamentos de equipamentos."
        ),
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER,
                description = "JWT Authorization header using Bearer scheme. Example: 'Bearer {token}'"
        )
})
public class OpenAPIConfig {
}