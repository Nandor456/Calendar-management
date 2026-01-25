package edu.idde.mnim2377.data.exception;

public class DataException extends RuntimeException {
    public DataException(String message) {
        super(message);
    }
    public DataException(Throwable throwable) {super(throwable);}
}
