package pl.mentor.banking.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder // Pozwala na wygodne tworzenie obiektu: TransactionSummary.builder().amount(...).build()
public class TransferRequest {
    @Schema(description = "Id użytkownika przelewającego")
    private Long fromUserId;
    @Schema(description = "Id użytkownika otrzymującego przelew")
    private Long toUserId;
    @Schema(description = "Przelewana kwota")
    private BigDecimal amount;
}
