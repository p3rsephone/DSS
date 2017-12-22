package business.exceptions;

public class InvalidPhaseException extends Exception {
    public InvalidPhaseException() {
        super();
    }

    public InvalidPhaseException(String message){
        super(message);
    }
}
