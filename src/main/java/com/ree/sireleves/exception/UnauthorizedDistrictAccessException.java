package com.ree.sireleves.exception;

/**
 * Exception thrown when an agent attempts to access a counter or resource
 * outside their assigned district.
 * This exception should result in a 403 Forbidden HTTP response.
 */
public class UnauthorizedDistrictAccessException extends RuntimeException {
    
    public UnauthorizedDistrictAccessException(String agentDistrict, String resourceDistrict) {
        super(String.format("Access denied: Agent district '%s' does not match resource district '%s'", 
            agentDistrict, resourceDistrict));
    }
    
    public UnauthorizedDistrictAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedDistrictAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
