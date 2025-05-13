package com.example.admin.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

	
	
	@ExceptionHandler(value =MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
	
	
	

	@ExceptionHandler(AppException.class)
    public ResponseEntity<CustomErrorResponse> handleAppException(AppException ex) {
		CustomErrorResponse  error = new CustomErrorResponse (ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

	
	/*
	 * @ExceptionHandler(value = Exception.class) public
	 * ResponseEntity<AppException> handleException(String msg){ AppException
	 * appex=new AppException();
	 * 
	 * appex.setExCode("EC001"); appex.setExDesc(msg);
	 * appex.setTimeAndDate(LocalDateTime.now());
	 * 
	 * 
	 * return new ResponseEntity<AppException>(appex,
	 * HttpStatus.INTERNAL_SERVER_ERROR); }
	 */
	
	
}
