package org.prd.ecommerce.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class SignupRequest {


    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 5, max = 64, message = "The field must be between 5 and 64 characters")
    @Email(message = "The field must be a valid email",regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @Length(min = 8,message = "The field must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "The field must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 4, max = 15, message = "The field must be between 4 and 15 characters")
    private String nickname;
}
