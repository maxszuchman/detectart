package com.experta.detectart.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExistentDeviceException extends RuntimeException {
    public ExistentDeviceException() {
        super();
    }

    public ExistentDeviceException(final String message) {
        super(message);
    }

    public ExistentDeviceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
