package org.prd.ecommerce.entities.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthenticationRequest {

    private String username;
    private String password;
}
