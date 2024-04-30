package org.prd.ecommerce.entities.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.prd.ecommerce.config.util.validation.ValidateUserRole;
import org.prd.ecommerce.config.util.enums.UserRole;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityDto {

    private Long id;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 4, max = 20, message = "The field must be between 4 and 20 characters")
    private String nickname;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 5, max = 64, message = "The field must be between 5 and 64 characters")
    @Email(message = "The field must be a valid email",regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 8,message = "The field must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "The field must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;


    @NotNull(message = "The field cannot be null")
    @ValidateUserRole(anyOf = {UserRole.ADMIN,UserRole.CUSTOMER},message = "The field must be any of {anyOf}")
    private UserRole userRole;

    @NotNull(message = "The field cannot be null")
    private boolean enabled;

    @NotNull(message = "The field cannot be null")
    private boolean accountNonExpired;

    @NotNull(message = "The field cannot be null")
    private boolean accountNonLocked;

    @NotNull(message = "The field cannot be null")
    private boolean credentialsNonExpired;

    private byte[] img;
}
