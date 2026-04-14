package pl.mentor.banking.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.mentor.banking.service.ReportService;

import java.rmi.AccessException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleEverythin(Exception ex){
        // My widzimy wszystko w logach (pełny Stacktrace)
        log.error("KRYTYCZNY BŁĄD NIEZNANY: ", ex);

        // Użytkownik widzi tylko bezpieczny komunikat
        return "Wystąpił nieoczekiwany błąd serwera. Spróbuj ponownie później.";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Ktoś przesłał błędne dane: {}", ex.getMessage()); // Używamy log.warn zamiast System.out
        return "Błąd walidacji: " + ex.getMessage();
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        log.warn("Próba pobrania raportu dla nieistniejącej waluty: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex){
        log.warn("Próba znalezienia nieistniejącego użytkownika: {} ", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex){
        log.warn("Błąd autoryzacji użytkownika: {}", ex.getMessage());
        return "Błąd autoryzacji: Nie masz uprawnień do przeglądania danych tego użytkownika.";
    }
}