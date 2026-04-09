package pl.mentor.banking.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder // Pozwala na wygodne tworzenie obiektu: TransactionSummary.builder().amount(...).build()
public class TransactionSummary {
    private String currency;
    private BigDecimal totalAmount;
    private long transactionCount;
}
