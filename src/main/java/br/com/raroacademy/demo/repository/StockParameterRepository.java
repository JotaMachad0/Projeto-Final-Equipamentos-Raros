package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.StockParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockParameterRepository extends JpaRepository<StockParameterEntity, Long> {
}
