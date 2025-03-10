package br.com.tax_calculator_API.infra.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:defaultSecretKey}")
    private String jwtSecret;

    @Value("${jwt.expiration:300000}")
    private long jwtExpirationDate;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);

            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            System.out.println("Token expirado: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Token não suportado: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("Token mal formado: " + ex.getMessage());
        } catch (SignatureException ex) {
            System.out.println("Assinatura inválida: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Token vazio: " + ex.getMessage());
        }
        return false;
    }
}