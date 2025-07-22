package br.com.raroacademy.demo.commons.annotations;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Tag(name = "")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiController {
    @AliasFor(annotation = Tag.class, attribute = "name")
    String name();
}
