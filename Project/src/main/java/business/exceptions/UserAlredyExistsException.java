package business.exceptions;

public class UserAlredyExistsException extends Throwable {
    public UserAlredyExistsException() {
        super();
    }

    public UserAlredyExistsException(String message){
        super(message);
    }
}
