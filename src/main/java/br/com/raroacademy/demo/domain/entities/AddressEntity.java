package br.com.raroacademy.demo.domain.entities;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    @Column(name = "zip_code")
    private String cep;
    private String country;

    @Column(name = "via_cep_response")
    @Type(JsonType.class)
    private String respostaViaCepJson;
}
