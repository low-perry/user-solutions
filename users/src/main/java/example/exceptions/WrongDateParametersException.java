package example.exceptions;

public class WrongDateParametersException extends RuntimeException{
    public WrongDateParametersException(String message) {
        super(message);
    }

    public WrongDateParametersException(String message, Throwable cause) {
        super(message, cause);
    }

}
