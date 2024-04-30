package org.prd.ecommerce.config.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE}) //La anotacion puede ser usada en estos elementos
@Retention(RUNTIME)//La anotacion estara disponible en tiempo de ejecucion
@Documented//La anotacion podra ser documentada
@Constraint(validatedBy = MultipartFilesValidation.class)
public @interface ValidateMultipartFiles {

    String message() default "Only PNG, WEBP or JPG images are allowed";//El mensaje de error por defecto

    Class<?>[] groups() default {};//Los grupos de validacion

    Class<? extends Payload>[] payload() default {};//La carga util de la anotacion
}
