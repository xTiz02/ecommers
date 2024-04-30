package org.prd.ecommerce.controller.exceptions.controll;


import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.*;

@RestControllerAdvice//esto es para que se aplique a todos los controladores
public class GlobalExceptionHandler{
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //controla las validaciones de los campos
    @ExceptionHandler(MethodArgumentNotValidException.class)//para que se ejecute cuando se de este error de validacion de datos de entidades
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest){
        //webRequest es para obtener la descripcion del error y mostrarla en el navegador
        HandlerMethod handlerMethod = (HandlerMethod) webRequest.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler", WebRequest.SCOPE_REQUEST);
        String controllerName = "",methodName="";
        if (handlerMethod != null) {
            controllerName = handlerMethod.getBeanType().getName();
            methodName = handlerMethod.getMethod().getName();
        }
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
        logger.warn(String.format("Validation error in: '%s'",controllerName), errosMap.toString());
        logger.warn(String.format("Method error: '%s'",methodName));
        logger.warn("Error:",ex);


        return new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                new ApiResponseBody(
                        (String.format("Errors in fields: %s",campos.toString())),
                        errosMap,
                        null,
                        StatusType.REQUEST_DATA_INVALID.name(),
                        HttpStatus.BAD_REQUEST.value()),
                webRequest.getDescription(false)),
                HttpStatus.BAD_REQUEST);
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
                StatusType.REQUEST_DATA_INVALID.name(),
                HttpStatus.BAD_REQUEST.value());
        logger.warn(String.format("Validation error in: '%s'",ex.getStackTrace()[0].getClassName()), message);
       return new ResponseEntity<>(new ApiResponse(
               (new Date().toString()),
               apiResponseBody,
               webRequest.getDescription(false)),
               HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)//controla cuando no se encuentra el path en el controlador
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest webRequest){
        String message = String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());
        //404
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                message,
                null,
                null,
                StatusType.ERROR.name(),
                HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                apiResponseBody,
                webRequest.getDescription(false)),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                new ApiResponseBody(
                        exception.getMessage(),
                        null,
                        null,
                        StatusType.DATA_EMPTY.name(),
                        HttpStatus.NOT_FOUND.value()),
                webRequest.getDescription(false)),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)//controla cuando algo salio mal en el servidor
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest webRequest){
        String message = String.format("Bad request: %s", ex.getMessage());
        //400
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                message,
                null,
                null,
                StatusType.REQUEST_DATA_INVALID.name(),
                HttpStatus.BAD_REQUEST.value());

        return  new ResponseEntity<>(new ApiResponse(
                (new Date().toString()),
                apiResponseBody,
                webRequest.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(HttpMessageNotReadableException.class)//controla cuando se pasa un json que no puede mapaear a un objeto
//    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest webRequest){
//        String message = String.format("Malformed JSON request");
//        //400
//        ApiResponseBody apiResponseBody = new ApiResponseBody(
//                message,
//                null,
//                null,
//                StatusType.REQUEST_DATA_INVALID.name(),
//                HttpStatus.BAD_REQUEST.value());
//
//        return new ResponseEntity<>(new ApiResponse(
//                (new Date().toString()),
//                apiResponseBody,
//                webRequest.getDescription(false)),
//                HttpStatus.BAD_REQUEST);
//    }







//    @ExceptionHandler(InternalServerException.class)//controla cuando algo salio mal en el servidor
//    public ResponseEntity<?> handleBadRequestException(InternalServerException ex, WebRequest webRequest){
//        String message = String.format("Internal error: %s", ex.getMessage());
//        //500
//        ApiResponseBody apiResponseBody = new ApiResponseBody(
//                message,
//                null,
//                null,
//                StatusType.INTERNAL_ERROR.name(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        return  new ResponseEntity<>(new ApiResponse(
//                (new Date().toString()),
//                apiResponseBody,
//                webRequest.getDescription(false)),
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }



//    @ExceptionHandler(Exception.class)//controla cualquier otro error
//    public ResponseEntity<?> handleException(Exception ex, WebRequest webRequest){
//        String message = String.format("-Unexpected error: %s", ex.getMessage());
//        //500
//        ApiResponseBody apiResponseBody = new ApiResponseBody(
//                message,
//                null,
//                null,
//                "error",
//                HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return new ResponseEntity<>(new ApiResponse(
//                (new Date().toString()),
//                apiResponseBody,
//                webRequest.getDescription(false)),
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
