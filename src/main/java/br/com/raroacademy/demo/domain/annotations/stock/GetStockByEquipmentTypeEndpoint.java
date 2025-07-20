package br.com.raroacademy.demo.domain.annotations.stock;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse200;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Operation(summary = "Get Stock by EquipmentType",
        description = "Endpoint responsible for retrieving a stock item by type",
        parameters = {
                @Parameter(name = "EquipmentType", example = "NOTEBOOK")
        }
)
@RequestMapping(method = RequestMethod.GET, path = "/{equipmentType}", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse404
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetStockByEquipmentTypeEndpoint {
}
