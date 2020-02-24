package Exceptions;

public class InvalidCashSum extends RuntimeException {
    public InvalidCashSum(String message) {
        super(message);
    }
}
