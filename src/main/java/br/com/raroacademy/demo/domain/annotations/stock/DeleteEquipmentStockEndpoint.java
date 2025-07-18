package br.com.raroacademy.demo.domain.annotations.stock;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse204;
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

@Operation(summary = "Delete equipment stock",
        description = "Endpoint responsible for deleting an equipment stock",
        parameters = {
                @Parameter(name = "id", example = "1", description = "Equipment stock ID")
        }
)
@RequestMapping(method = RequestMethod.DELETE, path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse204
@OpenApiResponse404
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteEquipmentStockEndpoint {
}
