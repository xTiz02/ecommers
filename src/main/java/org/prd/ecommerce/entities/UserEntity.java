package org.prd.ecommerce.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.prd.ecommerce.config.validation.ValidateUserRole;
import org.prd.ecommerce.enums.UserRole;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 4, max = 15, message = "The field must be between 4 and 15 characters")
    @Column(unique = true, nullable = false, length = 15)
    private String nickname;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 5, max = 64, message = "The field must be between 5 and 64 characters")
    @Email(message = "The field must be a valid email",regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Column(unique = true, nullable = false, length = 64)
    private String email;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 60, max = 70, message = "Password not valid")
    @Column(nullable = false, length = 70)
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "The field must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @ValidateUserRole(anyOf = {UserRole.ADMIN,UserRole.CUSTOMER},message = "The field must be any of {anyOf}")
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private boolean enabled;

    @Lob//indica que el campo es de tipo blob
    @Column(columnDefinition = "longblob")
    private byte[] img;
}
