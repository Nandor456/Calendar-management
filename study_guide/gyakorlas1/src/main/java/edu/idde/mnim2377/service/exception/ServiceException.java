package edu.idde.mnim2377.service.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Throwable e) { super(e); }
}
