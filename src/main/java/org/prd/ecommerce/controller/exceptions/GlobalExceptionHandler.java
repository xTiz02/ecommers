package org.prd.ecommerce.controller.exceptions;

import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.*;

@RestControllerAdvice//esto es para que se aplique a todos los controladores
public class GlobalExceptionHandler{
    //controla las validaciones de los campos
    @ExceptionHandler(MethodArgumentNotValidException.class)//para que se ejecute cuando se de este error
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest){
        //webRequest es para obtener la descripcion del error y mostrarla en el navegador

        List<String> campos = new ArrayList<>();
        Map<String, List<String>> errosMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            if(!campos.contains(field)){
                campos.add(field);
            }
            //String annotation = error.getCode();

            List<String> list = errosMap.get(field);
            if(list!=null){
                list.add(message);
            }else{
                list = new ArrayList<>();
                list.add(message);
            }
            errosMap.put(field,list);
        });
        //System.out.println(ex.getBindingResult().getFieldErrorCount());
        //System.out.println(ex.getBindingResult().getFieldErrors().toString());
        //400
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                (String.format("Error en los campos: %s",campos.toString())),
                errosMap,
                null,
                "error",
                HttpStatus.BAD_REQUEST.value());
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)//controla cunado el tipo de dato no es el correcto en la url de la solicitud
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest webRequest){
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("This '%s' should be a valid '%s' and '%s' isn't",
                name, type, value);
        //400
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                message,
                null,
                null,
                "error",
                HttpStatus.BAD_REQUEST.value());
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)//controla cuando no se encuentra el path en el controlador
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest webRequest){
        String message = String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());
        //404
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                message,
                null,
                null,
                "error",
                HttpStatus.NOT_FOUND.value());
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)//controla cuando algo salio mal mientras se espera la respuesta del servidor
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest webRequest){
        String message = String.format("Bad request: %s", ex.getMessage());
        //404
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                message,
                null,
                null,
                "error",
                HttpStatus.BAD_REQUEST.value());
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
