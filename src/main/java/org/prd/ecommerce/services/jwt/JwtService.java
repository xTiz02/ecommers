package org.prd.ecommerce.services.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

public interface JwtService {
     String create(Authentication auth) throws IOException;
     boolean validate(String token);
     Claims getClaims(String token);
     String getUsername(String token);
     Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    public String resolve(String token);//elimina el prefijo del token y devuelve el token sin el prefijo

    public void expireToken(String token);


}

