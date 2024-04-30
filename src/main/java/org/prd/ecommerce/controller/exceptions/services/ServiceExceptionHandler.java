package org.prd.ecommerce.controller.exceptions.services;

import jakarta.validation.ConstraintViolationException;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.BadRequestException;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class ServiceExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);
    //En esta capa se probaran distintas respuestas posibles hacia problemas internos del servidor.
    //Por ejemplo, si se viola alguna restriccion de alguna entidad validada, se devolvera un error 500.


//    @ExceptionHandler(ConstraintViolationException.class)//controla si se viola alguna restriccion de alguna entidad validada
//    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest){
//
//        //500
//        Map<String, List<String>> violationsMap = new HashMap<>();
//        ex.getConstraintViolations().forEach(mv->{
//            String fieldName = mv.getPropertyPath().toString();
//
//            if(violationsMap.containsKey(fieldName)){
//
//                List<String> list = violationsMap.get(fieldName);
//                list.add(mv.getMessage());
//                return;
//            }
//
//            violationsMap.put(fieldName, new ArrayList<>(List.of(mv.getMessage())));
//        });

//
//        /*System.out.println(ex.getStackTrace()[0].getClassLoaderName());
//        System.out.println(ex.getStackTrace()[0].getMethodName());//El metodo que lanzo la excepcion
//        System.out.println(ex.getStackTrace()[0].getFileName());//El archivo donde se lanzo la excepcion
//        System.out.println(ex.getStackTrace()[0].getLineNumber());//La linea donde se lanzo la excepcion
//        System.out.println(ex.getStackTrace()[0].getClass().getSimpleName());
//        System.out.println(ex.getStackTrace()[0].getClassName());//El nombre de la clase donde se lanzo la excepcion
//        */
//        return new ResponseEntity<>(new ApiResponse(
//                (new Date().toString()),
//                apiResponseBody,
//                webRequest.getDescription(false)),
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    @ExceptionHandler(ConstraintViolationException.class)//controla si se viola alguna restriccion de alguna entidad validada por @Valid por la request
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
        /*logger.error(String.format("Error occurred in: '%s'", ex.getStackTrace()[0].getClassName()));
        logger.error(String.format("Error occurred in method: '%s'", ex.getStackTrace()[0].getMethodName()));
        logger.error(String.format("Error occurred in file: '%s'", ex.getStackTrace()[0].getFileName()));
        logger.error(String.format("Error occurred in line: '%s'", ex.getStackTrace()[0].getLineNumber()));*/
        ex.printStackTrace();
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                String.format(ex.getMessage()),
                null,
                null,
                StatusType.INTERNAL_ERROR.name(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                apiResponseBody,
                webRequest.getDescription(false)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)//controla si se viola alguna restriccion de alguna entidad validada por la base de datos
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest webRequest) {

        logger.error("Error:",ex);
        //ex.getStackTrace()[0].getMethodName();//El metodo que lanzo la excepcion
//        String type = ex.getMostSpecificCause().getMessage();
//        int index = type.indexOf("for");
//        String message = String.format("Data restrict error: %s", type.substring(0, index - 1));

        ApiResponseBody apiResponseBody = new ApiResponseBody(
                ex.getMostSpecificCause().getMessage(),
                null,
                null,
                StatusType.INTERNAL_ERROR.name(),
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                apiResponseBody,
                webRequest.getDescription(false)),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateDataException.class)// controla si se repite un dato en la base de datos que no puede repetirse
    public ResponseEntity<?> handleDuplicateDataException(DuplicateDataException ex, WebRequest webRequest) {
        logger.error("Error:",ex);


        ApiResponseBody apiResponseBody = new ApiResponseBody(
                ex.getMessage(),
                new HashMap<>().put(ex.getField(), ex.getValue()),
                null,
                ex.getStatusType().name(),
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                apiResponseBody,
                webRequest.getDescription(false)),
                HttpStatus.CONFLICT);
    }



}
