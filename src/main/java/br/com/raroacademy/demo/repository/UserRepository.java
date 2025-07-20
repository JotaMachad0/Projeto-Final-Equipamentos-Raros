package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    List<UserEntity> findAllByEmailConfirmedTrue();
}
