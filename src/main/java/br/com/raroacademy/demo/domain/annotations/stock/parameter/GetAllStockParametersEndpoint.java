package br.com.raroacademy.demo.domain.annotations.stock.parameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Operation(
        summary = "Get all stock parameters",
        description = "Endpoint that returns all stock parameter records."
)
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "200", description = "List returned successfully")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetAllStockParametersEndpoint {}
