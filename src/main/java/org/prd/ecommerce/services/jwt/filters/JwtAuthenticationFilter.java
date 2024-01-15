package org.prd.ecommerce.services.jwt.filters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.AuthenticationRequest;
import org.prd.ecommerce.services.jwt.JwtService;
import org.prd.ecommerce.services.jwt.JwtServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
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
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = jwtService.create(authResult);
        //agregar Access-Control-Expose-Headers, para que el navegador pueda acceder a la cabecera Authorization
        response.addHeader(JwtServiceImpl.HEADER_STRING, JwtServiceImpl.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", JwtServiceImpl.HEADER_STRING); //usar esto para que el cliente pueda ver los datos del encabezado

//se añade el token en la cabecera de la respuesta para que el navegador lo guarde en el local storage y lo envie en la cabecera de las peticiones que haga al servidor
        Map<String, Object> body = new HashMap<String, Object>();
        //body.put("token", token);
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                String.format("Hola %s, has iniciado sesión con éxito!", ((User)authResult.getPrincipal()).getUsername()),
                null,
                authResult.getPrincipal(),
                "ok",
                HttpServletResponse.SC_OK);
        //body.put("user", (User) authResult.getPrincipal());
        //body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito!", ((User)authResult.getPrincipal()).getUsername()) );
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, request.getRequestURI());
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<String, Object>();
        ApiResponseBody apiResponseBody = new ApiResponseBody(
                failed.getMessage(),
                null,
                null,
                "error",
                HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse apiResponse = new ApiResponse((new Date().toString()),apiResponseBody, request.getRequestURI());


        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}
