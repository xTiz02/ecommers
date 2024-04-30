package org.prd.ecommerce.config.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFilesValidation implements ConstraintValidator<ValidateMultipartFiles, MultipartFile[]>{

    @Override
    public void initialize(ValidateMultipartFiles constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = true;
        if(multipartFiles==null){
            return true;
        }
        if(multipartFiles!=null){
            for (MultipartFile multipartFile: multipartFiles) {
                String contentType = multipartFile.getContentType();
                if (!isSupportedContentType(contentType)) {
                    result = false;
                    break;
                }
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
}
