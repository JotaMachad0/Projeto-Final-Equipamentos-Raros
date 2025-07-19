package br.com.raroacademy.demo.domain.annotations.equipmentCollaborator;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Get all equipment-collaborator links",
        description = "Endpoint responsible for searching for all equipment-collaborator links")
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetAllEquipmentCollaboratorEndpoint {
}