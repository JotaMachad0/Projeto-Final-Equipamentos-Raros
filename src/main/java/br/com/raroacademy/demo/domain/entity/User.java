package br.com.raroacademy.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "email_confirmed")
    private Boolean emailConfirmed;

    @Builder
    public User(Long id, String name, String email, String password, Boolean emailConfirmed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.emailConfirmed = emailConfirmed;
    }
}
