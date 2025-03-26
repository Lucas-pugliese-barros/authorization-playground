package br.com.pugliese.authorization.controller;

import br.com.pugliese.authorization.dto.request.AuthenticationRequest;
import br.com.pugliese.authorization.dto.request.TokenRequest;
import br.com.pugliese.authorization.dto.response.AuthenticationResponse;
import br.com.pugliese.authorization.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {

        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody TokenRequest tokenRequest) {

        return ResponseEntity.ok(service.validate(tokenRequest));
    }
}
