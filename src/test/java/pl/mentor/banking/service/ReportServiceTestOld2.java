package pl.mentor.banking.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mentor.banking.exception.CurrencyNotFoundException;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ReportServiceTestOld2 {
/*
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void shouldReturnSummaryWhenCurrencyExists() {
        // GIVEN (Dano) - przygotowujemy dane
        String currency = "PLN";
        TransactionSummary expectedSummary = new TransactionSummary(currency, BigDecimal.valueOf(100), 1L);

        // Mówimy Mockito: "Gdy ktoś zapyta o PLN, zwróć Optional z naszym expectedSummary"
        Mockito.when(transactionRepository.getSummaryForCurrency(currency))
                .thenReturn(Optional.of(expectedSummary));

        // WHEN (Gdy) - wywołujemy naszą testowaną metodę
        TransactionSummary result = reportService.getCurrencyReport(currency);

        // THEN (Wtedy) - sprawdzamy czy wynik jest taki jak chcieliśmy (używamy Assertions)
        // Podpowiedź: assertEquals(expectedSummary.getCurrency(), result.getCurrency());
        // 1. Czy waluta się zgadza? (To już masz)
        assertEquals(expectedSummary.getCurrency(), result.getCurrency());

        // 2. Czy suma kwot jest taka sama?
        // Porównujemy 100 (z expectedSummary) z tym, co zwrócił serwis (result)
        assertEquals(expectedSummary.getTotalAmount(), result.getTotalAmount());

        // 3. Czy liczba transakcji się zgadza?
        // Porównujemy 1 (z expectedSummary) z tym, co zwrócił serwis (result)
        assertEquals(expectedSummary.getTransactionCount(), result.getTransactionCount());

        Mockito.verify(transactionRepository, Mockito.times(1)).getSummaryForCurrency(currency);
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotFound(){
        // GIVEN (Dano) - przygotowujemy dane
        String currency = "JPY";

        // Mówimy Mockito: "Gdy ktoś zapyta o JPY, zwróć puste pudełko"
        Mockito.when(transactionRepository.getSummaryForCurrency(currency))
                .thenReturn(Optional.empty());

        // WHEN & THEN (W testowaniu wyjątków te kroki często się łączą)
        // Sprawdzamy, czy wywołanie metody rzuci konkretny wyjątek
        CurrencyNotFoundException exception = assertThrows(CurrencyNotFoundException.class, () ->{
            reportService.getCurrencyReport(currency);
        });
        log.info("exception: {}", exception.getMessage());
        // Dodatkowo sprawdzamy, czy wiadomość w błędzie jest taka, jak zaprogramowałeś
        assertTrue(exception.getMessage().contains("Bank nie posiada transakcji w walucie: JPY"));
        Mockito.verify(transactionRepository, Mockito.times(1)).getSummaryForCurrency(currency);
    }

*/

}
