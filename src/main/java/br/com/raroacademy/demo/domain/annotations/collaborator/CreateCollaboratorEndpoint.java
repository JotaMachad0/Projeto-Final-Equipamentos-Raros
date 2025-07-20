package br.com.raroacademy.demo.domain.annotations.collaborator;

import br.com.raroacademy.demo.commons.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "create collaborator",
        description = "Endpoint responsible for adding new collaborator."
)
@RequestMapping(method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse201
@OpenApiResponse400
@OpenApiResponse409
@OpenApiResponse422
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateCollaboratorEndpoint {
}



