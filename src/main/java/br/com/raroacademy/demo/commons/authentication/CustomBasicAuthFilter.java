package br.com.raroacademy.demo.commons.authentication;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.exception.ErrorMessage;
import br.com.raroacademy.demo.exception.UnauthorizedException;
import br.com.raroacademy.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Base64;


@AllArgsConstructor
@Service
public class CustomBasicAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final I18nUtil i18n;
    private final LocaleResolver localeResolver;

    private static final int BASIC_LENGTH = 6;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            LocaleContextHolder.setLocale(localeResolver.resolveLocale(request));

            var headerAuthorization = request.getHeader("Authorization");

            if (headerAuthorization == null || !headerAuthorization.startsWith("Basic ")) {
                filterChain.doFilter(request, response);
                return;
            }

            var basicToken = headerAuthorization.substring(BASIC_LENGTH);
            byte[] basicTokenDecoded = Base64.getDecoder().decode(basicToken);
            String basicTokenValue = new String(basicTokenDecoded);

            String[] basicAuthsSplit = basicTokenValue.split(":");
            if (basicAuthsSplit.length != 2) {
                throw new UnauthorizedException(i18n.getMessage("invalid.credentials"));
            }

            String email = basicAuthsSplit[0];
            String password = basicAuthsSplit[1];

            var user = userRepository.findByEmail(email);
            if (user == null || !user.getPassword().equals(password) || !user.getEmailConfirmed()) {
                throw new UnauthorizedException(i18n.getMessage("invalid.credentials"));
            }

            var authentication = new UsernamePasswordAuthenticationToken(email, password, java.util.Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException ex) {
            ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.UNAUTHORIZED, ex.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getOutputStream(), errorMessage);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

}
