package org.prd.ecommerce.controller;

import jakarta.validation.Valid;
import org.prd.ecommerce.controller.exceptions.BadRequestException;
import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody @Valid SignupRequest signupRequest){
        try{
            Optional<UserEntityDto> userDto = authService.createUser(signupRequest);
            return new ResponseEntity<>(userDto.get(), HttpStatus.CREATED);

        }catch (DataAccessException exDt) {
            throw new BadRequestException(exDt.getLocalizedMessage());
        }
    }
    @GetMapping("/test/{id}")
    public ResponseEntity<?> test(@PathVariable("id") Long id){
        return new ResponseEntity<>("Test",HttpStatus.OK);
    }

    @GetMapping("/hellCheck")
    public ResponseEntity<?> hellCheck(){
        return ResponseEntity.ok("La api funciona correctamente1");
    }
}
