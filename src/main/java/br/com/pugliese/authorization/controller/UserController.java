package br.com.pugliese.authorization.controller;

import br.com.pugliese.authorization.dto.request.RegisterUserRequest;
import br.com.pugliese.authorization.dto.request.UpdateUserRequest;
import br.com.pugliese.authorization.dto.request.UserResponse;
import br.com.pugliese.authorization.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        UserResponse userResponse = service.register(registerUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> update(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = service.updateUser(authorizationHeader, updateUserRequest);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
