package com.example.admin.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
