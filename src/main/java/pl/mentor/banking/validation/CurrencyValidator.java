package pl.mentor.banking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class CurrencyValidator implements ConstraintValidator<SupportedCurrency, String> {

    private final List<String> allowedCurrencies = List.of("PLN", "EUR", "USD");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return allowedCurrencies.contains(value.toUpperCase());
    }
}