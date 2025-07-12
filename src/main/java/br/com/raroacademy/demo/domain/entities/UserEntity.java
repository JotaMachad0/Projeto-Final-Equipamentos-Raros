package br.com.raroacademy.demo.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "email_confirmed")
    private Boolean emailConfirmed;

    @Builder
    public UserEntity(Long id, String name, String email, String password, Boolean emailConfirmed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.emailConfirmed = emailConfirmed;
    }
}
