package business.exceptions;

public class StudentsDoNotFitInShiftException extends Throwable {
    public StudentsDoNotFitInShiftException() {
        super();
    }

    public StudentsDoNotFitInShiftException(String message) {
        super(message);
    }
}
