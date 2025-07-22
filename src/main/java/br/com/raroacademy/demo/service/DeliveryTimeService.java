package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.enums.Region;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DeliveryTimeService {

    public LocalDate calculate(String estado, LocalDate dataDeEnvio) {
        if (dataDeEnvio == null) {
            return null;
        }

        Region regiao = Region.fromEstado(estado);
        int diasDePrazo = regiao.getPrazoEmDias();

        return dataDeEnvio.plusDays(diasDePrazo);
    }
}