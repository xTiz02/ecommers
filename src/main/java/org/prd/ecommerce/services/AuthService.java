package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.UserEntity;
import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

public interface AuthService {
    Optional<UserEntityDto> createUser(SignupRequest signupRequest) ;
}
