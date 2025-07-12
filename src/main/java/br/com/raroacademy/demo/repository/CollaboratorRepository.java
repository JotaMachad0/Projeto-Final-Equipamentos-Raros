package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaboratorRepository extends JpaRepository<CollaboratorEntity, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
