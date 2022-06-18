package com.ghostcoderz.blog_application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTTokenHelper {

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private final String secret = "jwtTokenKey";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token,
                                    Function<Claims, T> claimsResolver
                ) {

        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(this.secret)
                .parseClaimsJws(token).getBody();
    }

    //check if the token is expired
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /*While creating the token -
        1. Define claims of the token, like Issuer, Expiration, Subject and ID
        2. Sign the JWT using the HS512 Algorithm and secret key
        3. According to JWS Compact Serialization compaction of the JWT (https://datatracker.ietf.org/doc/search?csrfmiddlewaretoken=VQ50PH8MAPRYfAt88r61ATCls6V206drBzrlUaNk0oVgOFzqQjcLXUa1a25W85n6&name=jose&sort=&rfcs=on&activedrafts=on&by=group&group=)
        to a URL-safe string
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() +
                                        JWT_TOKEN_VALIDITY * 1000)
                )
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate Token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        boolean isUsernameEqual = username.equals(userDetails.getUsername());

        return isUsernameEqual && !isTokenExpired(token);
    }

}
