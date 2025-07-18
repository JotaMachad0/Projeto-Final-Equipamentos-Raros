package br.com.raroacademy.demo.domain.annotations.stock.allert;

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

@Operation(summary = "Mark stock alert as processed",
        description = "Endpoint responsible for updating the status of a stock alert from 'Criado' to 'Processado'",
        parameters = {
                @Parameter(name = "id", example = "1", description = "Stock alert ID")
        }
)
@RequestMapping(method = RequestMethod.PUT, path = "/{id}/process", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse404
@OpenApiResponse409
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessStockAlertEndpoint {
}
