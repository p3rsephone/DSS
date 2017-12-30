package business.exceptions;

public class StudentNotInShiftException extends Exception {
    public StudentNotInShiftException() {
        super();
    }

    public StudentNotInShiftException(String message){
        super(message);
    }
}
