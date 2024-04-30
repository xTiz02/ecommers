package org.prd.ecommerce.config.util.validation;

import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ValidatingService<T> {
    private Logger logger = LoggerFactory.getLogger(ValidatingService.class);

    public void validateInput(T input) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            Map<String, List<String>> violationsMap = new HashMap<>();

            violations.forEach(mv ->{
                String fieldName = mv.getPropertyPath().toString();

                if(violationsMap.containsKey(fieldName)){

                    List<String> list = violationsMap.get(fieldName);
                    list.add(mv.getMessage());
                    return;
                }
                violationsMap.put(fieldName, new ArrayList<>(List.of(mv.getMessage())));
            });
            logger.error("Internal Map validation failed: {}", violationsMap);
            logger.error(String.format("Error for validate class: '%s'",input.getClass().getSimpleName()));//nombre de la clase del input : input

            //throw new ConstraintViolationException(input.getClass().getSimpleName(),violations);
            throw new ConstraintViolationException("Internal validation error occurred",violations);
        }
        logger.info(String.format("Internal validation passed for class: '%s'",input.getClass().getSimpleName()));
    }
}
