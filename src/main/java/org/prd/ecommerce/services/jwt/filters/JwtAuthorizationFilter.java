package org.prd.ecommerce.services.jwt.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.prd.ecommerce.services.jwt.JwtService;
import org.prd.ecommerce.services.jwt.JwtServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtService jwtService;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtServiceImpl.HEADER_STRING);

        if (!requiresAuthentication(header)) {


            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;

        if(jwtService.validate(header)) {
            authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null, jwtService.getRoles(header));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
    protected boolean requiresAuthentication(String header) {

        if (header == null || !header.startsWith(JwtServiceImpl.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
