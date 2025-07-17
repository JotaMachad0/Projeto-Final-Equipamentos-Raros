package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.StockParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockParameterRepository extends JpaRepository<StockParameter, Long> {
}
