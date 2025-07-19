package br.com.raroacademy.demo.domain.annotations.equipment;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;
import br.com.raroacademy.demo.commons.annotations.*;

import java.lang.annotation.*;

@Operation(
        summary = "List equipment",
        description = "Endpoint responsible for listing all registered equipment."
)
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse200
@OpenApiResponse400
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetAllEquipmentsEndpoint {
}
