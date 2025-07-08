package br.com.raroacademy.demo.domain.repository;

import br.com.raroacademy.demo.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
