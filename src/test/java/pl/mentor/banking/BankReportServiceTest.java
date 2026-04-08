package pl.mentor.banking;

import org.junit.jupiter.api.Test;
import pl.mentor.banking.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankReportServiceTest {

    @Test
    void shouldSumTransactionsInCorrectCurrency(){
        // 1. Arrange (Przygotuj dane)
        var service = new BankReportService();
        var transactions = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("99.99"), "USD", LocalDateTime.now())
        );

        // 2. Act (Wykonaj akcję)
        BigDecimal result = service.sumTransaction(transactions, "PLN");

        // 3. Assert (Sprawdź wynik) - Oczekujemy 150.00 (100 + 50), USD powinno być pominięte
        assertEquals(new BigDecimal("150.00"), result, "Suma transakcji w PLN powinna wynosić 150.00");
    }

    @Test
    void shouldReturnEmptyOptionalWhenListIsEmpty(){
        var service = new BankReportService();
        List<Transaction> emptyList = List.of();
        var result = service.getLongestCurrencyNameTransaction(emptyList);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnZeroWhenNoTransactionMatchesCurrency(){

        var service = new BankReportService();
        var transactions = List.of(
                new Transaction(new BigDecimal("120.00"), "USD", LocalDateTime.now()),
                new Transaction(new BigDecimal("30.00"), "USD", LocalDateTime.now()),
                new Transaction(new BigDecimal("99.99"), "USD", LocalDateTime.now())
        );

        var result = service.sumTransaction(transactions, "PLN");
        assertTrue(BigDecimal.ZERO.compareTo(result) == 0, "Wynik powinien być matematycznie równy zero");
    }

    @Test
    void shouldGroupAndSumByCurrency(){
        var service = new BankReportService();
        var transaction = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("10.12"), "USD", LocalDateTime.now())
        );

        var result = service.sumByCurrency(transaction);
        assertEquals(2, result.size());
        assertTrue(result.get("PLN").compareTo(new BigDecimal("150.00"))==0);
        assertTrue(result.get("USD").compareTo(new BigDecimal("10.12"))==0);

    }

    @Test
    void shouldReturnMapWithListOfAmountsForEachCurrency() {
        var service = new BankReportService();
        var transaction = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("10.12"), "USD", LocalDateTime.now())
        );

        var result = service.currencyList(transaction);

        assertEquals(2, result.size());
        var plnAmounts = result.get("PLN");
        assertEquals(2, plnAmounts.size());
        assertTrue(plnAmounts.contains(new BigDecimal("100.00")));
        assertTrue(plnAmounts.contains(new BigDecimal("50.00")));
    }

    @Test
    void shouldCountTransactionsAboveThresholdPerCurrency() {
        var service = new BankReportService();
        var transaction = List.of(
                new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("150.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("200.00"), "USD", LocalDateTime.now())
        );

        var result = service.countLargeTransactionsByCurrency(transaction, new BigDecimal("100.00"));

        assertEquals(2L, result.size());
        var plnAmounts = result.get("PLN");
        assertEquals(1L, plnAmounts);
    }

    @Test
    void shouldReturnMaxTransactionPercurrency(){
        var service = new BankReportService();

        var transaction = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("500.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("200.00"), "USD", LocalDateTime.now()),
                new Transaction(new BigDecimal("1000.00"), "USD", LocalDateTime.now())
        );

        var result = service.getMostExpensiveTransactionByCurrency(transaction);

        assertEquals(2, result.size());
        assertNotNull(result.get("PLN"), "Mapa powinna zawierać klucz PLN - sprawdź literówki w danych!");
        var plnResult = result.get("PLN");
        assertTrue(plnResult.isPresent(), "Optional dla PLN nie powinien być pusty");

        assertTrue(new BigDecimal("500.00").compareTo(plnResult.get().getAmount()) == 0);
        assertEquals(0, new BigDecimal("500.00").compareTo(plnResult.get().getAmount()));
    }
}
