package br.com.pugliese.authorization.service;

import br.com.pugliese.authorization.dto.request.RegisterUserRequest;
import br.com.pugliese.authorization.dto.response.UserResponse;
import br.com.pugliese.authorization.dto.user.Role;
import br.com.pugliese.authorization.entity.User;
import br.com.pugliese.authorization.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterUserRequest registerUserRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        registerUserRequest = new RegisterUserRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "password123"
        );

        savedUser = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponse response = userService.register(registerUserRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());

        // Verify interactions
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    void testRegisterUser_BuildsUserCorrectly() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Act
        UserResponse response = userService.register(registerUserRequest);

        // Assert
        assertNotNull(response);

        // Verify the user object was built correctly before saving
        verify(userRepository).save(argThat(user -> {
            assertEquals("John", user.getFirstname());
            assertEquals("Doe", user.getLastname());
            assertEquals("john.doe@example.com", user.getEmail());
            assertEquals("encodedPassword", user.getPassword());
            assertEquals(Role.USER, user.getRole());
            return true;
        }));
    }

    @Test
    void testRegisterUser_NullRequest_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userService.register(null);
        });

        // Verify no interactions with mocks
        verifyNoInteractions(passwordEncoder, userRepository);
    }
}