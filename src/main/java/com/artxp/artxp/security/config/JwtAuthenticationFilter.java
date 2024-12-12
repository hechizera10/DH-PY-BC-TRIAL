package com.artxp.artxp.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter dar la oportunidad de q jwtfilter se ejecute o sea parte de esa cadena de filtros
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    //Este filtro esta en medio de los filtros, viene de un filtro anterior y luego se lo pasa a otro
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        //En el header de las request siempre nos va a llegar el token
        final String authHeader = request.getHeader("Authorization"); // para buscar si exise el token
        final String token;
        final String userEmail;

        //Si no existe el token o q no comience con bearer le decimos que pase al otro filtro
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }


        token = authHeader.substring(7); //desde la posicion 7 en adelante va a ser mi token
        userEmail = jwtService.extractUsername(token);
        if(userEmail!= null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); //obtenemos datos del user, authorities

            if(jwtService.isTokenValid(token, userDetails)){
                //esto es para guardarlo dentro del context holder y tener la info del user en lo q va y viene la request
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response); //Damos paso al prox filtro
        }


    }
}
