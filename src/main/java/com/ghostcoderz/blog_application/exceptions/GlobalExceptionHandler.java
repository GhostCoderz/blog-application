package com.ghostcoderz.blog_application.exceptions;

import com.ghostcoderz.blog_application.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(
            ResourceNotFoundException ex
    ){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex
    ){
        HashMap<String, String> errorList = new HashMap<>();

        ex.getAllErrors().stream().forEach(e -> {
            String objectName = ((FieldError)e).getField();
            String message = e.getDefaultMessage();
            errorList.put(objectName, message);
        });

        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

}
