package com.ghostcoderz.blog_application.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(UserDetailsService userDetailsService, JWTTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Token from Request
        String requestToken = request.getHeader("Authorization");
        System.out.println("Request Token : " + requestToken);

        // JWT token is like :
        //Bearer <token>
        // JWT Token starts from the 7th index of the requestToken
        String username = null;
        String token = null;

        if( requestToken!=null && requestToken.startsWith("Bearer")) {

            token = requestToken.substring(7);
            try {
                username = this.jwtTokenHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException ie){
                System.out.println("Unable to get the JWT token");
            } catch (ExpiredJwtException eje){
                System.out.println("JWT Token has expired");
            } catch (MalformedJwtException mje){
                System.out.println("Invalid JWT token");
            }

        } else {
            System.out.println("JWT token does not starts with Bearer");
        }

        //After getting token, validate the token
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(this.jwtTokenHelper.validateToken(token, userDetails)){

                UsernamePasswordAuthenticationToken
                        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


            } else {
                System.out.println("Invalid JWT Token");
            }

        } else {
            System.out.println("Username is null or Security Context is not null");
        }

        filterChain.doFilter(request, response);
    }

}
