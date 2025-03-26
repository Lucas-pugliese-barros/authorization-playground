package br.com.pugliese.authorization.service;

import br.com.pugliese.authorization.dto.request.*;
import br.com.pugliese.authorization.dto.request.UserResponse;
import br.com.pugliese.authorization.dto.user.Role;
import br.com.pugliese.authorization.entity.User;
import br.com.pugliese.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterUserRequest registerUserRequest) {
        User user = User.builder()
                .firstname(registerUserRequest.getFirstName())
                .lastname(registerUserRequest.getLastName())
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = repository.save(user);

        return new UserResponse(savedUser.getFirstname(),
                savedUser.getLastname(),
                savedUser.getEmail());
    }

    public UserResponse updateUser(String authorizationHeader, UpdateUserRequest updateUserRequest) {
        return null;
    }
}
