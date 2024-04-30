package org.prd.ecommerce.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.prd.ecommerce.config.util.validation.ValidateMultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 4, max = 15, message = "The field must be between 4 and 15 characters")
    private String name;

    @Length(max = 70, message = "The field must be less than 70 characters")
    @NotEmpty(message = "The field cannot be empty")
    private String description;
    
    private String imgUrl;

    @ValidateMultipartFile
    private MultipartFile img;


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedAt;



}
