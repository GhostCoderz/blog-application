package com.ghostcoderz.blog_application.controller;

import com.ghostcoderz.blog_application.payload.JWTAuthRequest;
import com.ghostcoderz.blog_application.payload.JWTAuthResponse;
import com.ghostcoderz.blog_application.security.JWTTokenHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final JWTTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JWTTokenHelper jwtTokenHelper, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> createToken(
            @RequestBody JWTAuthRequest request
            ) throws Exception {

        System.out.println("Request Details : \n" + request.getUsername() + "\n" + request.getPassword());

        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(request.getUsername());

        String token = this.jwtTokenHelper.generateToken(userDetails);

        JWTAuthResponse response = new JWTAuthResponse(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken
                usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e){
            System.out.println("Invalid Login Details");
            throw new Exception("Invalid username or password");
        }

    }

}
