package net.supertabs.server.auth;

import net.supertabs.server.SupertabsException;

public class InvalidCredentialsException extends SupertabsException {
    private static final String ERROR_STRING = "InvalidCredentials";
    private static final String ERROR_MSG = "Credentials could not be verified!";
    
    public InvalidCredentialsException() {
        super(ERROR_MSG);
        this.error_string = ERROR_STRING;
    }

    public InvalidCredentialsException(String message) {
        super(message);
        this.error_string = ERROR_STRING;
    }

    public InvalidCredentialsException(Throwable cause) {
        super(ERROR_MSG, cause);
        this.error_string = ERROR_STRING;
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
        this.error_string = ERROR_STRING;
    }
}
