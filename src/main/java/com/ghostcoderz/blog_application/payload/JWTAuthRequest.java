package com.ghostcoderz.blog_application.payload;

import lombok.Data;

@Data
public class JWTAuthRequest {

    private String username;
    private String password;

}
