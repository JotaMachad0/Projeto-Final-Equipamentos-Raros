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

@Operation(summary = "Send email reset password",
        description = "Endpoint responsible for sending email with confirmation code to reset the user's password"
)
@RequestMapping(method = RequestMethod.POST,path = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse204
@OpenApiResponse404
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SendEmailResetEndpoint {
}
