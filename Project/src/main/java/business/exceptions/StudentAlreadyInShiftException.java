package business.exceptions;

public class StudentAlreadyInShiftException extends Exception {
    public StudentAlreadyInShiftException() {
        super();
    }

    public StudentAlreadyInShiftException(String message){
        super(message);
    }
}
