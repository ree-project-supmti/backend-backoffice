package com.ree.sireleves.exception;

/**
 * Exception thrown when a reading submission fails validation.
 * This exception should result in a 400 Bad Request HTTP response.
 */
public class InvalidReadingException extends RuntimeException {
    
    public InvalidReadingException(String message) {
        super(message);
    }
    
    public InvalidReadingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates an exception for invalid index value.
     */
    public static InvalidReadingException invalidIndex(Integer newIndex, Integer previousIndex) {
        return new InvalidReadingException(
            String.format("New index (%d) must be greater than or equal to previous index (%d)", 
                newIndex, previousIndex)
        );
    }
    
    /**
     * Creates an exception for duplicate reading.
     */
    public static InvalidReadingException duplicateReading(String mobileUuid) {
        return new InvalidReadingException(
            String.format("Reading with mobile UUID '%s' already exists", mobileUuid)
        );
    }
    
    /**
     * Creates an exception for inactive counter.
     */
    public static InvalidReadingException inactiveCounter(Long counterId) {
        return new InvalidReadingException(
            String.format("Cannot submit reading for inactive counter with ID: %d", counterId)
        );
    }
}
