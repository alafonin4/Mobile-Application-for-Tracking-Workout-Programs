package ru.alafonin4.authserver.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements AuthRequest{
    @Email(message = "Incorrect email address.")
    private String email;
    @NotBlank(message = "Password cannot be empty.")
    private String password;
}
