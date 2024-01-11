package org.prd.ecommerce.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.prd.ecommerce.enums.UserRole;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE}) //La anotacion puede ser usada en estos elementos
@Retention(RUNTIME)//La anotacion estara disponible en tiempo de ejecucion
@Documented//La anotacion podra ser documentada
@Constraint(validatedBy = UserRoleValidation.class)//La anotacion sera validada por la clase UserRoleValidation
public @interface ValidateUserRole {
    UserRole[] anyOf();//El valor de la anotacion sera un array de UserRole (enum) llamado anyOf (puede ser cualquier cosa)
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
