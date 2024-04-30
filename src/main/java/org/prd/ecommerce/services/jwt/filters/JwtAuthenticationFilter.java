package org.prd.ecommerce.services.jwt.filters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.BadRequestException;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.AuthenticationRequest;
import org.prd.ecommerce.entities.entity.Audit;
import org.prd.ecommerce.repository.AuditRepository;
import org.prd.ecommerce.services.jwt.JwtService;
import org.prd.ecommerce.services.jwt.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private AuditRepository auditRepository;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, AuditRepository auditRepository) {
        this.authenticationManager = authenticationManager;
        this.auditRepository = auditRepository;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth/login", "POST"));

        this.jwtService = jwtService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if(username != null && password !=null) {
            //cuando se envia el username y password por form-data
            logger.info("Username desde request parameter (form-data): " + username);
            logger.info("Password desde request parameter (form-data): " + password);

        } else {
            //cuando se envia el username y password por raw (json)
            AuthenticationRequest userRequest = null;
            try {

                userRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);

                //generar json del request
                username = userRequest.getUsername();
                password = userRequest.getPassword();

                logger.info("Username desde request InputStream (raw): " + username);
                logger.info("Password desde request InputStream (raw): " + password);

            } catch (JsonParseException e) {
                responseError(response, request, "Malformed body data request1", e);
                return null;
            } catch (JsonMappingException e) {//
                responseError(response, request, "Malformed body data request2", e);
                return null;
            } catch (IOException e) {
                responseError(response, request, "Malformed body data request3", e);
                return null;

            }
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);//se envia el token al AuthenticationManager para que lo valide
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = jwtService.create(authResult);
        //agregar Access-Control-Expose-Headers, para que el navegador pueda acceder a la cabecera Authorization
        response.addHeader(JwtServiceImpl.HEADER_STRING, JwtServiceImpl.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", JwtServiceImpl.HEADER_STRING); //usar esto para que el cliente pueda ver los datos del encabezado

//se añade el token en la cabecera de la respuesta para que el navegador lo guarde en el local storage y lo envie en la cabecera de las peticiones que haga al servidor
        User user = (User) authResult.getPrincipal();
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ApiResponse(
                (new Date().toString()),
                new ApiResponseBody(String.format("Hola %s, has iniciado sesión con éxito!", user.getUsername()),
                    null,
                    authResult.getPrincipal(),
                    StatusType.OK.name(),
                    HttpServletResponse.SC_OK),
                request.getRequestURI())));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        auditRepository.save(new Audit(user.getUsername(), "login", null, new Date(), null, "User logged in successfully"));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        //AuthenticationException es una clase abstracta que se usa para manejar las excepciones de autenticacion como:
        //BadCredentialsException, DisabledException, LockedException, etc
        //se puede usar para manejar las excepciones de autenticacion personalizadas

        //en el request podemos recuperar el username y password que se envio en la peticion por json
        //String username = obtainUsername(request); por form-data


        String mensaje = "",status = StatusType.ERROR.name();
        if(failed instanceof BadCredentialsException) {
            mensaje = "Error: El nombre de usuario o la contraseña son incorrectos";
            status = StatusType.BAD_CREDENTIALS.name();
        }
        if (failed instanceof DisabledException) {
            mensaje = "Error: La cuenta no esta habilitada";
            status = StatusType.ACCOUNT_DISABLED.name();
        }
        if (failed instanceof LockedException) {
            mensaje = "Error: La cuenta ha sido bloqueada";
            status = StatusType.ACCOUNT_LOCKED.name();
        }
        if (failed instanceof AccountExpiredException) {
            mensaje = "Error: La cuenta ha expirado";
            status = StatusType.ACCOUNT_EXPIRED.name();
        }
        if (failed instanceof CredentialsExpiredException) {
            mensaje = "Error: Las credenciales de la cuenta han expirado";
            status = StatusType.CREDENTIALS_EXPIRED.name();
        }
        logger.warn(String.format("Authentication error: %s",mensaje));
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                failed.getMessage(),
                null,
                null,
                status,
                HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, request.getRequestURI());


        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }


    public void responseError(HttpServletResponse response, HttpServletRequest request, String msg, Exception e){
        logger.error("Error:",e);
        try {

            response.getWriter().write(new ObjectMapper().writeValueAsString(new ApiResponse(
                    (new Date().toString()),
                    new ApiResponseBody(
                            String.format(msg),
                            null,
                            null,
                            StatusType.REQUEST_DATA_INVALID.name(),
                            HttpServletResponse.SC_BAD_REQUEST),
                    request.getRequestURI())));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
    }
}
