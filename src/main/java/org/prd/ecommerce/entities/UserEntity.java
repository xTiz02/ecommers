package org.prd.ecommerce.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.prd.ecommerce.config.validation.ValidateUserRole;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.enums.UserRole;

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


    @ValidateUserRole(anyOf = {UserRole.ADMIN,UserRole.CUSTOMER},message = "The field must be any of {anyOf}")
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private boolean enabled;

    @Lob//indica que el campo es de tipo blob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    public UserEntity( @Valid  UserEntityDto userEntityDto){
        this.nickname = userEntityDto.getNickname();
        this.email = userEntityDto.getEmail();
        this.password = userEntityDto.getPassword();
        this.userRole = userEntityDto.getUserRole();
        this.enabled = userEntityDto.isEnabled();
        this.img = userEntityDto.getImg();
    }
}
