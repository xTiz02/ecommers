package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
@AllArgsConstructor
public class ApiResponse {
    private String tiempo;
    private ApiResponseBody body;
    private String url;


}
