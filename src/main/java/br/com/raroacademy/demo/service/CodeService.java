package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.CodeEntity;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.exception.CodeException;
import br.com.raroacademy.demo.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    private final I18nUtil i18n;

    public Long addUserAndCode(UserEntity user) {

        var random = new Random();
        Long codeNumber = random.nextLong(900000) + 100000;

        var code = codeRepository.findByUser(user);

        if (code == null) {
            code = new CodeEntity();
            code.setToken(codeNumber.toString());
            code.setCreatedAt(LocalDateTime.now());
            code.setExpiresAt(LocalDateTime.now().plusHours(2));
            code.setUsed(false);
            code.setUser(user);
            codeRepository.save(code);

        } else {
            code.setToken(codeNumber.toString());
            code.setExpiresAt(LocalDateTime.now().plusHours(2));
            code.setUsed(false);
            codeRepository.save(code);
        }
        return codeNumber;
    }

    public void verifyAndConfirmCode(Long code, UserEntity user) {
        var codeUser = codeRepository.findByUser(user);

        if (codeUser == null) {
            throw new CodeException(i18n.getMessage("code.invalid"));
        }

        if (!code.toString().equals(codeUser.getToken())) {
            throw new CodeException(i18n.getMessage("code.invalid"));
        }

        if (codeUser.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CodeException(i18n.getMessage("code.expired"));
        }
        
        if (codeUser.getUsed() == true ) {
            throw new CodeException(i18n.getMessage("code.already.used"));
        }
        codeUser.setUsed(true);
    }

    public void confirmCode(Long token, UserEntity user) {
        var codeUser = codeRepository.findByUser(user);

        if (!token.toString().equals(codeUser.getToken())) {
            throw new CodeException(i18n.getMessage("code.invalid"));
        }
    }
}
