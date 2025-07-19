package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.commons.authentication.JwtTokenProvider;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.auth.AuthRequestDTO;
import br.com.raroacademy.demo.domain.DTO.auth.AuthResponseDTO;
import br.com.raroacademy.demo.domain.DTO.auth.MapperAuth;
import br.com.raroacademy.demo.domain.annotations.auth.LoginEndpoint;
import br.com.raroacademy.demo.exception.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenApiController(name = "Auth")
@ApiController(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MapperAuth mapperAuth;
    private final I18nUtil i18n;

    @LoginEndpoint
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.createToken(request.email());

            return ResponseEntity.ok(
                    mapperAuth.toResponseDTO(
                            token,
                            "Bear",
                            jwtTokenProvider.getExpirationDateFromToken(token).getTime() / 1000,
                            request.email())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(i18n.getMessage("invalid.credentials"));
        }
    }
}