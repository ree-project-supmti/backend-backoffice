package com.ree.sireleves.exception;

/**
 * Exception thrown when a counter is not found in the system.
 * This exception should result in a 404 Not Found HTTP response.
 */
public class CounterNotFoundException extends RuntimeException {
    
    public CounterNotFoundException(Long counterId) {
        super("Counter not found with ID: " + counterId);
    }
    
    public CounterNotFoundException(String message) {
        super(message);
    }
    
    public CounterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
