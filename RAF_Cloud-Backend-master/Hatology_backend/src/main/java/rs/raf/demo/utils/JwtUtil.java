package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserService;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "MY JWT SECRET";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static SecureRandom random = new SecureRandom();
    private final UserService userService;

    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return null;
        }

    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){

        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenExpiredd(String token){

        Claims c = extractAllClaims(token);

        if (c == null) {
            System.out.println("Token jeste istekao");
            return true;
        }else {
            System.out.println("Token nije istekao");
            return c.getExpiration().before(new Date());
        }
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        User usr = userService.findByEmail(username);
        claims.put("createProductPermission", usr.getAdminPermission());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public String generatePageToken(){
        Map<String, Object> claims = new HashMap<>();

        claims.put("token", new BigInteger(130, random).toString(32));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
