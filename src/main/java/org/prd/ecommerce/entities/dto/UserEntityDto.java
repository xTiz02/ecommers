package org.prd.ecommerce.entities.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.prd.ecommerce.config.validation.ValidateUserRole;
import org.prd.ecommerce.enums.UserRole;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityDto {

    private Long id;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 4, max = 15, message = "The field must be between 4 and 15 characters")
    private String nickname;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 5, max = 64, message = "The field must be between 5 and 64 characters")
    @Email(message = "The field must be a valid email",regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 60, max = 70, message = "Password not valid")
    private String password;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @ValidateUserRole(anyOf = {UserRole.ADMIN,UserRole.CUSTOMER},message = "The field must be any of {anyOf}")
    private UserRole userRole;
    @NotNull(message = "The field cannot be null")
    private boolean enabled;

    private byte[] img;
}
