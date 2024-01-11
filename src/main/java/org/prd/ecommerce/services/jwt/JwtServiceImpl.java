package org.prd.ecommerce.services.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class JwtServiceImpl implements JwtService{

    public static final String SECRET = "1232463562451341241847918346918365981349817398461938649136491834691834691836491836498136491836498136498136498134691834698163498163498134";
    public static final String TOKEN_PREFIX = "Bearer "; // prefijo del token en la cabecera de la petición
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_DATE = 14000000L;


    @Override
    public String create(Authentication auth) throws IOException {
        String username = ((User) auth.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        Claims claims = Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)//es el username del usuario autenticado en el sistema (email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE)) // 4 horas
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes()).compact();


    }


    @Override
    public boolean validate(String token) {
        try {

            getClaims(token);
            //con los claims se valida el token porque si el token es valido, se puede extraer los claims del token
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes())
                .parseClaimsJws(resolve(token)).getBody();
        return claims;
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
        Object roles = getClaims(token).get("authorities");

        Collection<? extends GrantedAuthority> authorities = Arrays
                .asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                        .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

        return authorities;
    }
    @Override
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    @Override
    public Date extractExpiration(String token){
        return getClaims(token).getExpiration();
    }


    @Override
    public String resolve(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }
}
