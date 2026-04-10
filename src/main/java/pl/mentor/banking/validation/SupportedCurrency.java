package pl.mentor.banking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER}) // Gdzie można użyć (pola i parametry)
@Retention(RetentionPolicy.RUNTIME) // Kiedy ma działać (podczas pracy programu)
@Constraint(validatedBy = CurrencyValidator.class) // Kto wykonuje logikę
public @interface SupportedCurrency {
    String message() default "Waluta nie jest obsługiwana przez nasz bank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}