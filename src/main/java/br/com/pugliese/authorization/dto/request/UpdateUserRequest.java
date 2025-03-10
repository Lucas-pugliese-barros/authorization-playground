package br.com.pugliese.authorization.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotEmpty
    private String token;

    private String firstname;

    private String lastname;

    private String email;

    private String password;
}
