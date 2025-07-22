package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
