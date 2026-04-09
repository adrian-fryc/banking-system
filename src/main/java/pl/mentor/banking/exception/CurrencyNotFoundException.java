package pl.mentor.banking.exception;

// Własny wyjątek - teraz wiadomo, o co chodzi!
public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String currency) {
        super("Bank nie posiada transakcji w walucie: " + currency);
    }
}