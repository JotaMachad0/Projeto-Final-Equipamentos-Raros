package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.CodeEntity;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, Long> {
    CodeEntity findByUser(UserEntity user);
}
