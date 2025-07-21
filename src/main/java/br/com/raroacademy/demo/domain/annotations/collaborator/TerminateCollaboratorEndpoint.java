package br.com.raroacademy.demo.domain.annotations.collaborator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colaborador desligado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
        @ApiResponse(responseCode = "404", description = "Colaborador não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
})
public @interface TerminateCollaboratorEndpoint {
    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Desliga um colaborador e retorna seus empréstimos ativos";
}