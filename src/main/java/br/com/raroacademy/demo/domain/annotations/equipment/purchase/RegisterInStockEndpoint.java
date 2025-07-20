package br.com.raroacademy.demo.domain.annotations.equipment.purchase;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Register Purchase in Stock",
        description = "Endpoint responsible for registering an equipment purchase in stock and updating its status to REGISTERED",
        parameters = {
                @Parameter(name = "id", example = "1")
        }
)
@RequestMapping(method = RequestMethod.POST, path = "/{id}/register-in-stock", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse404
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterInStockEndpoint {
}