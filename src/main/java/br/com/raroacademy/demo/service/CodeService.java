package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.CodeEntity;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository resetTokensRepository;

    private final I18nUtil i18n;

    public Long addUserAndToken(UserEntity user) {

        var random = new Random();
        Long codeNumber = random.nextLong(900000) + 100000;

        var code = resetTokensRepository.findByUser(user);

        if (code == null) {
            code = new CodeEntity();
            code.setToken(codeNumber.toString());
            code.setCreatedAt(LocalDateTime.now());
            code.setExpiresAt(LocalDateTime.now().plusHours(2));
            code.setUsed(false);
            code.setUser(user);
            resetTokensRepository.save(code);

        } else {
            code.setToken(codeNumber.toString());
            code.setExpiresAt(LocalDateTime.now().plusHours(2));
            code.setUsed(false);
            resetTokensRepository.save(code);
        }
        return codeNumber;
    }
}
