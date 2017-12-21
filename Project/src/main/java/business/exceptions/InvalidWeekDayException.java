package business.exceptions;

public class InvalidWeekDayException extends Exception {
    public InvalidWeekDayException() {
        super();
    }

    public InvalidWeekDayException(String message){
        super(message);
    }
}
