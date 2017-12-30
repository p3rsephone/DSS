package business.exceptions;

public class ExchangeOutdatedException extends Exception {
    public ExchangeOutdatedException() {
        super();
    }

    public ExchangeOutdatedException(String message){
        super(message);
    }
}
