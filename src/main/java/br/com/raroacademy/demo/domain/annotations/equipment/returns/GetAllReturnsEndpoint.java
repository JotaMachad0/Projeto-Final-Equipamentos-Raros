package br.com.raroacademy.demo.domain.annotations.equipment.returns;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
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
        summary = "List all equipment returns",
        description = "Endpoint responsible for retrieving a list of all return records."
)
@RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@OpenApiResponse200
public @interface GetAllReturnsEndpoint {
}