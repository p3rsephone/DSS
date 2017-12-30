package business.exceptions;

public class RoomCapacityExceededException extends Exception {
    public RoomCapacityExceededException() {
        super();
    }

    public RoomCapacityExceededException(String message){
        super(message);
    }
}
