package com.ghostcoderz.blog_application.exceptions;

public class APIException extends RuntimeException {

    public APIException() { }

    public APIException(String message) {
        super(message);
    }
}
