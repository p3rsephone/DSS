package business.exceptions;

public class RequestInvalidException extends Exception {
    public RequestInvalidException() {
        super();
    }

    public RequestInvalidException(String message){
        super(message);
    }
}
