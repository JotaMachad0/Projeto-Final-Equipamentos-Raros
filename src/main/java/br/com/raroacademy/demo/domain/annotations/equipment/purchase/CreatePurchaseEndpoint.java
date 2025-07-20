package br.com.raroacademy.demo.domain.annotations.equipment.purchase;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse201;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse400;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse422;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Create Purchase",
        description = "Endpoint responsible for adding new equipment purchase."
)
@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse201
@OpenApiResponse400
@OpenApiResponse422
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatePurchaseEndpoint {
}