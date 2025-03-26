package br.com.pugliese.authorization.controller;

import br.com.pugliese.authorization.dto.request.RegisterUserRequest;
import br.com.pugliese.authorization.dto.request.UpdateUserRequest;
import br.com.pugliese.authorization.dto.request.UserResponse;
import br.com.pugliese.authorization.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Handles user registration and update")
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterUserRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {

        UserResponse userResponse = service.register(registerUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update user information",
            description = "Updates the user's profile information",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    @PutMapping("/update")
    public ResponseEntity<UserResponse> update(
            @RequestHeader(name = "Authorization", required = true) String authorizationHeader,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        UserResponse userResponse = service.updateUser(authorizationHeader, updateUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
