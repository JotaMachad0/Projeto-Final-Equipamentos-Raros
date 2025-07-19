package br.com.raroacademy.demo.domain.annotations.equipment_purchase;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse422;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Update Purchase",
        description = "Endpoint responsible for updating the equipment purchase.",
        parameters = {
                @Parameter(name = "id", example = "1")
        }
)
@RequestMapping(method = RequestMethod.PUT, path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse404
@OpenApiResponse422
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdatePurchaseEndpoint {
}