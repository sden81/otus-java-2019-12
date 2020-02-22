package Exceptions;

public class InvalidCurrency extends RuntimeException{
    public InvalidCurrency(String message) {
        super(message);
    }
}
