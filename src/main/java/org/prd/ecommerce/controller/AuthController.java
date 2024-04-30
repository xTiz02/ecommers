package org.prd.ecommerce.controller;

import jakarta.validation.Valid;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.BadRequestException;

import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody @Valid SignupRequest signupRequest){
//        try{
            UserEntityDto userDto = authService.createUser(signupRequest);
            return new ResponseEntity<>(new ApiResponse(
                    (new Date()).toString(),
                    new ApiResponseBody(
                            "User register",
                            null,
                            userDto,
                            StatusType.OK.name(),
                            HttpStatus.CREATED.value()),
                    "/api/auth/signup"),
                    HttpStatus.CREATED);

//        }catch (DataAccessException exDt) {
//            exDt.printStackTrace();
//            throw new InternalServerException(exDt.getMessage());
//        }
    }
    @GetMapping("/test/{id}")
    public ResponseEntity<?> test(@PathVariable("id") Long id){
        return new ResponseEntity<>("Test",HttpStatus.OK);
    }

    @GetMapping("/hellCheck")
    public ResponseEntity<?> hellCheck(){
        return ResponseEntity.ok("La api funciona correctamente");
    }
}
