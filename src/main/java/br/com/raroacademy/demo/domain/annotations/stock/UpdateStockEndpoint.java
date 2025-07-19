package br.com.raroacademy.demo.domain.annotations.stock;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse409;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse422;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Operation(summary = "Update Stock",
        description = "Endpoint responsible for updating a stock by its type",
        parameters = {
                @Parameter(name = "EquipmentType", example = "NOTEBOOK")
        }
)
@RequestMapping(method = RequestMethod.PUT, path = "/{EquipmentType}", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse404
@OpenApiResponse409
@OpenApiResponse422
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateStockEndpoint {
}
