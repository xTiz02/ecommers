package org.prd.ecommerce.config.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.prd.ecommerce.config.util.enums.UserRole;

import java.util.Arrays;

public class UserRoleValidation implements ConstraintValidator<ValidateUserRole, UserRole> {

    private UserRole[] userRoles;
    @Override
    public void initialize(ValidateUserRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.userRoles = constraintAnnotation.anyOf();//El valor de la anotacion sera un array de UserRole (enum) llamado anyOf (puede ser cualquier cosa)
    }

    @Override
    public boolean isValid(UserRole userRole, ConstraintValidatorContext constraintValidatorContext) {

        return userRole == null || Arrays.asList(userRoles).contains(userRole);
    }
}
