package business.exceptions;

public class RoomFullException extends Exception{

    public RoomFullException() {
        super();
    }

    public RoomFullException(String message)     y {
        super(message);
    }
}
