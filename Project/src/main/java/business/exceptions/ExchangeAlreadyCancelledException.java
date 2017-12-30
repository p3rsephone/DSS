package business.exceptions;

public class ExchangeAlreadyCancelledException extends Exception {
    public ExchangeAlreadyCancelledException() {
        super();
    }

    public ExchangeAlreadyCancelledException(String message){
        super(message);
    }
}
