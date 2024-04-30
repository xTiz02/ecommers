package org.prd.ecommerce.config.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.prd.ecommerce.config.util.enums.UserRole;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileValidation implements ConstraintValidator<ValidateMultipartFile, MultipartFile> {
    @Override
    public void initialize(ValidateMultipartFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = true;
        if(multipartFile==null){
            return true;
        }
        if(multipartFile!=null){
            String contentType = multipartFile.getContentType();
            if (!isSupportedContentType(contentType)) {
                result = false;
            }
        }else {
            result = false;
        }
        return result;


    }
    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/webp");
    }
//    contentType.equals("text/xml")
//            || contentType.equals("application/pdf")
}
