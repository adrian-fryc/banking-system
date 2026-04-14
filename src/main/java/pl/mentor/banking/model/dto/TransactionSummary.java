package pl.mentor.banking.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder // Pozwala na wygodne tworzenie obiektu: TransactionSummary.builder().amount(...).build()
public class TransactionSummary {
    @Schema(description = "Nazwa waluty w formacie ISO (np. PLN, EUR, USD)")
    private String currency;
    @Schema(description = "Suma wszystkich transakcji dla danej waluty", example = "1500.50")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal amount;
    @Schema(description = "Kurs waluty docelowej użyty do przeliczenia raportu", example = "3.20")
    BigDecimal exchangeRate;
}
