package br.com.raroacademy.demo.domain.annotations.equipment;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;
import br.com.raroacademy.demo.commons.annotations.*;

import java.lang.annotation.*;

@Operation(
        summary = "Create equipment",
        description = "Endpoint responsible for registering a new equipment."
)
@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse201
@OpenApiResponse400
@OpenApiResponse409
@OpenApiResponse422
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateEquipmentEndpoint {
}
