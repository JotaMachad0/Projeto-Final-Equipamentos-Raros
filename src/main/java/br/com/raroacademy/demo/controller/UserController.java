package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.user.ChangePasswordRequestDTO;
import br.com.raroacademy.demo.domain.DTO.user.SendEmailRequestDTO;
import br.com.raroacademy.demo.domain.DTO.user.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.user.UserResponseDTO;
import br.com.raroacademy.demo.domain.annotations.user.*;
import br.com.raroacademy.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenApiController(name = "Users")
@ApiController(path = "/users")
@AllArgsConstructor
public class UserController {

    public final UserService userService;

    @CreateUserEndpoint
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        var response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetUserEndpoint
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @UpdateUserEndpoint
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request
    ) {
        var response = userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteUserEndpoint
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetAllUserEndpoint
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @SendEmailResetEndpoint
    public ResponseEntity<Void> sendEmailResetPassword(@Valid @RequestBody SendEmailRequestDTO request ){
        userService.sendEmailResetPassword(request);
        return ResponseEntity.ok().build();
    }

    @ChangePasswordEndpoint
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request ){
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @ConfirmEmailEndpoint
    public ResponseEntity<Void> confirmEmail(@RequestParam String email, @RequestParam Long token) {
        userService.confirmEmail(email, token);
        return ResponseEntity.ok().build();
    }

    @ResendConfirmEmailEndpoint
    public ResponseEntity<Void> resendConfirmationEmail(@Valid @RequestBody SendEmailRequestDTO request) {
        userService.resendConfirmationEmail(request);
        return ResponseEntity.ok().build();
    }
}
