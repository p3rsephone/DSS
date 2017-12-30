package business.exceptions;

public class ShiftNotValidException extends Exception {
    public ShiftNotValidException() {
        super();
    }

    public ShiftNotValidException(String message){
        super(message);
    }
}
