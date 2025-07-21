package br.com.raroacademy.demo.domain.annotations.equipment.returns;

import br.com.raroacademy.demo.commons.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Process a received equipment",
        description = "Endpoint responsible for updating a return record with the receipt date and equipment status."
)
@RequestMapping(
        method = RequestMethod.POST,
        path = "/{id}/process",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@OpenApiResponse200
@OpenApiResponse400
@OpenApiResponse404
@OpenApiResponse409
public @interface ProcessReturnEndpoint {
}