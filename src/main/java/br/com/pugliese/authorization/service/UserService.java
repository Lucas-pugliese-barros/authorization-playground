package br.com.pugliese.authorization.service;

import br.com.pugliese.authorization.dto.request.*;
import br.com.pugliese.authorization.dto.response.UserResponse;
import br.com.pugliese.authorization.dto.user.Role;
import br.com.pugliese.authorization.entity.User;
import br.com.pugliese.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
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

        return new UserResponse(savedUser.getId(),
                savedUser.getFirstname(),
                savedUser.getLastname(),
                savedUser.getEmail());
    }
}
