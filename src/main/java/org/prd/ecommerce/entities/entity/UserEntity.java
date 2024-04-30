package org.prd.ecommerce.entities.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.prd.ecommerce.config.util.validation.ValidateUserRole;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.config.util.enums.UserRole;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor

@ToString

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false, length = 15)
    private String nickname;


    @Column(unique = true, nullable = false, length = 64)
    private String email;


    @Column(nullable = false, length = 70)
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "The field must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;



    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;


    private String url;


}
