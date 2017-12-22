package business.exceptions;

public class ExchangeDoesNotExistException extends Exception {
    public ExchangeDoesNotExistException() {
        super();
    }

    public ExchangeDoesNotExistException(String message){
        super(message);
    }
}
