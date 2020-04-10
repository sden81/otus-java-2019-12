package core;

public class Hw10CommonException extends RuntimeException {
    public Hw10CommonException(String message) {
        super(message);
    }

    public Hw10CommonException(Exception e) {
        super(e);
    }
}
