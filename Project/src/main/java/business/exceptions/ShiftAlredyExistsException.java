package business.exceptions;

public class ShiftAlredyExistsException extends Exception {
    public ShiftAlredyExistsException() {
        super();
    }

    public ShiftAlredyExistsException(String message){
        super(message);
    }
}
