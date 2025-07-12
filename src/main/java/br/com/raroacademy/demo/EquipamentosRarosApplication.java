package br.com.raroacademy.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EquipamentosRarosApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquipamentosRarosApplication.class, args);
    }
}
