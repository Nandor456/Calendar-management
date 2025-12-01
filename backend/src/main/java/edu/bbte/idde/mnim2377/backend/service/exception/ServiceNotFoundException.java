package edu.bbte.idde.mnim2377.backend.service.exception;

public class ServiceNotFoundException extends ServiceException {
    public ServiceNotFoundException(String message) {
        super(message, null);
    }
}
