package br.com.raroacademy.demo.commons.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RequestMapping
@RestController
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiController {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {};
}
