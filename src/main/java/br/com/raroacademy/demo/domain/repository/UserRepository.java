package br.com.raroacademy.demo.domain.repository;

import br.com.raroacademy.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
