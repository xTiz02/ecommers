package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;

import java.util.Optional;

public interface AuthService {
    UserEntityDto createUser(SignupRequest signupRequest) ;
}
