package br.com.raroacademy.demo.domain.annotations.user;

import br.com.raroacademy.demo.commons.annotations.OpenApiResponse204;
import br.com.raroacademy.demo.commons.annotations.OpenApiResponse404;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Resend confirmation email", description = "Resend confirmation email with a new token")
@RequestMapping(method = RequestMethod.POST, path = "/resend-confirm-email", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenApiResponse204
@OpenApiResponse404
@SecurityRequirements
public @interface ResendConfirmEmailEndpoint {
}