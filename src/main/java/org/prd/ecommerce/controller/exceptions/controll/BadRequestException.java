package org.prd.ecommerce.controller.exceptions.controll;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.Date;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)//400
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }



}
