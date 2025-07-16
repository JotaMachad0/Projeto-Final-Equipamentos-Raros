package br.com.raroacademy.demo.domain.annotations.expected.hiring;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse409;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Mark expected hiring as processed",
        description = "Endpoint responsible for updating the status of an expected hiring from 'Criada' to 'Processada'",
        parameters = {
                @Parameter(name = "id", example = "1", description = "ID da previsão de contratação")
        }
)
@RequestMapping(method = RequestMethod.PUT, path = "/{id}/start", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse404
@OpenApiResponse409
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartExpectedHiringEndpoint {
}
