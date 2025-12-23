package edu.bbte.idde.mnim2377.service.exception;

public class ServiceNotFoundException extends ServiceException {
    public ServiceNotFoundException(String message) {
        super(message, null);
    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
