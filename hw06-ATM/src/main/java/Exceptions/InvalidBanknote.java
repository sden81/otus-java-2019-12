package Exceptions;

public class InvalidBanknote extends ATMException{
    public InvalidBanknote(String message) {
        super(message);
    }
}
