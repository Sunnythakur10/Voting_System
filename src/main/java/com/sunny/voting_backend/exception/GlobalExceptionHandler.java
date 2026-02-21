package com.sunny.voting_backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
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
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String , Object>> handleDuplicateEntry(DataIntegrityViolationException ex){
        Map <String , Object> errorResponse = new HashMap<>();
        errorResponse.put("status" , HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message" , "Operation failed: Data already exists or constraint violated."+ex.getMessage());
        errorResponse.put("timeStamp" , System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyVotedException.class)
    public ResponseEntity<Map<String , Object>> handleDuplicateVote(UserAlreadyVotedException ex){
        Map <String , Object> errorResponse = new HashMap<>();
        errorResponse.put("status" , HttpStatus.BAD_REQUEST.value());
        errorResponse.put("Message" , ex.getMessage());
        errorResponse.put("timeStamp" , System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String , String> errorResponse = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.put(fieldName , errorMessage);
        });

        return new ResponseEntity<>(errorResponse , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String , String>> handleGeneralError(Exception ex){
        Map<String , String> errorResponse = new HashMap<>();
        errorResponse.put("status" , "500");
        errorResponse.put("message" , "Something went Wrong,  "+ex.getMessage());
        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
