package br.com.raroacademy.demo.domain.annotations.user;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse204;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Reset user password",
        description = "Endpoint responsible for changing the user's password using a confirmation code"
)
@RequestMapping(method = RequestMethod.POST,path = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse204
@OpenApiResponse404
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangePasswordEndpoint {
}
