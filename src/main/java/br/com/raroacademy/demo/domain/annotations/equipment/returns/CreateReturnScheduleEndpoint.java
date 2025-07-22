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
        summary = "Schedule an equipment return",
        description = "Endpoint responsible for creating a record for a future equipment return."
)
@RequestMapping(
        method = RequestMethod.POST,
        path = "/schedule",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@OpenApiResponse201
@OpenApiResponse400
@OpenApiResponse404
@OpenApiResponse409
public @interface CreateReturnScheduleEndpoint {
}