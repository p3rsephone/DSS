package business.exceptions;

public class StudentAlredyInShiftException extends Exception {
    public StudentAlredyInShiftException() {
        super();
    }

    public StudentAlredyInShiftException(String message){
        super(message);
    }
}
