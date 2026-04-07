package pl.mentor.banking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp
) {
    // To jest Record - odpowiednik Groovy @Immutable, ale wbudowany w język.
    // Nie potrzebuje getterów, setterów, ani toString().
}
