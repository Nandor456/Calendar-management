package edu.bbte.idde.mnim2377.backend.data.exception;

public class DataException extends Exception {
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(String message) {
        super(message);
    }
}
