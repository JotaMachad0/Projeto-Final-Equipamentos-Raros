package br.com.raroacademy.demo.commons.authentication;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final I18nUtil i18n;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null || !user.getEmailConfirmed()) {
            throw new NotFoundException(i18n.getMessage("user.not.found.or.email.not.confirmed"));
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}