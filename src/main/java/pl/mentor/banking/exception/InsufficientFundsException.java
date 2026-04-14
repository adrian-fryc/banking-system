package pl.mentor.banking.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String e) {
        super("Kwota obciążenia musi być dodatnia");
    }
}
