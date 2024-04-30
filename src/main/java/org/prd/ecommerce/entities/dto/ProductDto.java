package org.prd.ecommerce.entities.dto;


import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;
import org.prd.ecommerce.config.util.validation.ValidateMultipartFile;
import org.prd.ecommerce.config.util.validation.ValidateMultipartFiles;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    @NotEmpty(message = "The field cannot be empty")
    @NotNull(message = "The field cannot be null")
    @Length(min = 4, max = 40, message = "The field must be between 4 and 40 characters")
    private String name;


    @NotEmpty(message = "The field cannot be empty")
    private String description;

    private String characteristics;

    private String specifications;

    @NotNull(message = "The field cannot be null")
    private Double price;

    private String principalUrl;

    private String namePrincipal;

    private boolean enabled;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedAt;

    @ValidateMultipartFiles
    private MultipartFile[] images;

    @NotNull(message = "The field cannot be null")
    private Long categoryId;

    private String categoryName;
    private Integer stock;
    List<String> imagesUrl;

}
