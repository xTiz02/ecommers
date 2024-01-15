package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;
@Data
@ToString
@AllArgsConstructor
public class ApiResponseBody {
    private String message;
    private Object fields;
    private Object data;
    private String type;
    private int status;


}
