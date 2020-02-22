package Exceptions;

public class InvalidBanknote extends RuntimeException{
    public InvalidBanknote(String message) {
        super(message);
    }
}
