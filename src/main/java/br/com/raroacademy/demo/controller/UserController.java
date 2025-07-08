package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.UserResponseDTO;
import br.com.raroacademy.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Validated @RequestBody UserRequestDTO request) {
        var response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
