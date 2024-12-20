package com.artxp.artxp.security.config;

import com.artxp.artxp.domain.entities.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final SecretKey key = Jwts.SIG.HS256.key().build(); //creamos key escondida de todos

    //Generamos el token
    public String generateToken(UserDetails userDetails) {
        UsuarioEntity usuario = (UsuarioEntity) userDetails;
        //Podemos crear nuestras propias claims las claims que queramos manualmente
        Map<String, Object> claims = Map.of(
                "rol", userDetails.getAuthorities(),
                "nombre", usuario.getName(),
                "apellido", usuario.getLastname(),
                "email", usuario.getEmail()
        );

        //Armamos el token
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(key) //metodo de verificación
                .compact();
    }

    //Desarmado del token
    //Ej. Cuando viene request se necesita verificar si el token es válido y pertenece al user
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }
}
