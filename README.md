# Authorization Service API

![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)
![JWT](https://img.shields.io/badge/JWT-Authentication-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

Welcome to the **Authentication Service API**, a robust and secure RESTful API built with Spring Boot for user registration, authentication, and token validation. This project leverages JWT (JSON Web Tokens) for secure authentication and provides a clean, scalable architecture for managing user-related operations.

---

## ✨ Features

- **User Registration**: Create new user accounts with validated input.
- **Authentication**: Secure login with email and password, returning a JWT token.
- **Token Validation**: Verify the validity of JWT tokens.
- **Spring Security**: Integrated for authentication.
- **OpenAPI (Swagger)**: API documentation for easy exploration and testing.
- **Lombok**: Reduces boilerplate code for cleaner development.

---

## 🛠️ Tech Stack

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Security**: For authentication
- **JWT (io.jsonwebtoken)**: Token generation and validation
- **Lombok**: For concise code
- **Jakarta Validation**: Input validation
- **Maven**: Dependency management

---

## 🚀 Getting Started
Prerequisites
Java 17+: Ensure JDK is installed (java -version to check).
Maven: For dependency management and building (mvn -v to check).
Git: To clone the repository.

- Installation

Clone the Repository:
```
git clone https://github.com/your-username/authorization-service.git
cd authorization-service
```

- Build the Project:
```
mvn clean install
```

- Start the DB
```
docker compose -f docker-compose.yml up
```

- Run the Application:
```
mvn spring-boot:run
```

---

# 📖 API Documentation

The **Authorization Service API** provides interactive API documentation using Swagger UI, generated from OpenAPI annotations in the source code.


## Accessing the Documentation

- **URL**: `http://localhost:8080/swagger-ui.html`
- **Description**: Once the application is running, visit this URL in your browser to explore and test all available endpoints interactively.

---

# 🔒 Security


## Security Features

- **JWT Authentication**:
    - Tokens are signed with a secret key using the HS256 algorithm.
    - Tokens expire after 24 hours (configurable in `JwtService`).
    - Managed by the `JwtService` class for generation and validation.

- **Password Encoding**:
    - Passwords are securely hashed using `PasswordEncoder` (e.g., BCrypt).
    - Handled in `AuthenticationService` during user authentication.

- **Spring Security**:
    - Integrated framework for authentication and authorization.
    - Configurable via a `SecurityConfig` class (recommended to implement, not shown in the provided code snippet).

---

## Configuration

- **JWT Secret**: Defined in `application.yml` or hardcoded in `JwtService`:
  ```yaml
  jwt:
    secret: "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970" # Replace in production