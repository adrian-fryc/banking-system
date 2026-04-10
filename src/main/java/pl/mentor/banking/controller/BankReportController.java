package pl.mentor.banking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.service.ReportService;
import pl.mentor.banking.TransactionRecord;
import pl.mentor.banking.model.entity.Transaction;
import pl.mentor.banking.validation.SupportedCurrency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController // Mówi Springowi: "Ta klasa obsługuje zapytania HTTP i zwraca dane (zwykle JSON)"
@RequestMapping("/api/reports") // Wszystkie adresy w tej klasie będą zaczynać się od /api/reports
@Validated
@Tag(name = "Raporty", description = "Zarządzanie raportami bankowymi")
public class BankReportController {

    private final ReportService reportService;


    public BankReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/test")
    public String test(){
        return "Serwer działa! Możemy generować raporty";
    }

    @GetMapping("/sum")
    public Map<String, BigDecimal> getSum(){
        // Udajemy, że pobraliśmy to z bazy danych
        var mockTransactions = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("250.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("10.00"), "USD", LocalDateTime.now())
        );

        // Wywołujemy Twoją logikę streamową!
        return reportService.sumByCurrency(mockTransactions);
    }

    @GetMapping("/single")
    public TransactionRecord getSingle() {
        return new TransactionRecord(new BigDecimal("100.00"), "PLN", LocalDateTime.now());
    }

    @PostMapping("/calculate")
    public Map<String, BigDecimal> calculateSum(@Valid @RequestBody List<Transaction> transactions) {
        // Spring bierze JSON-a z Twojego zapytania i zamienia go na listę obiektów Transaction
        return reportService.calculateAndSave(transactions);
    }

    @GetMapping("/all")
    public List<Transaction> getHistory(){
        return reportService.getAllTransactions();
    }

    @GetMapping("/filter")
    public List<Transaction> getFilteredHistory(@Valid @RequestParam String currency){
        return reportService.findByCurrency(currency);
    }

    @GetMapping("/grater")
    public List<Transaction> getAmountGreaterThan(@Valid @RequestParam BigDecimal amount){
        return reportService.findByAmountGreaterThan(amount);
    }

    @GetMapping("/greaterCurrency")
    public List<Transaction> getCurrencyAndAmountGreaterThan(@Valid @RequestParam String currency, @Valid @RequestParam(required = false) BigDecimal amount){
        return reportService.findByCurrencyAndAmountGreaterThan(currency, amount);
    }

    @GetMapping("/date")
    public List<Transaction> getHistoryAfterDate(@Valid @RequestParam LocalDateTime date){
        return reportService.findByTimestampAfter(date);
    }

    @GetMapping("/less")
    public List<Transaction> getAmountLessThan(@Valid @RequestParam BigDecimal amount){
        return reportService.findByAmountLessThan(amount);
    }

    @DeleteMapping("/deleteCurrency/{currency}")
    public void deleteByCurrency(@Valid @PathVariable String currency){
        reportService.deleteByCurrency(currency);
    }

    @PutMapping("/update/{id}")
    public void setAmount(@PathVariable  Long id, @RequestParam @Positive BigDecimal newAmount){
        reportService.updateAmount(id, newAmount);
    }

    @GetMapping("/summary")
    @Operation(description = "Pobiera podsumowanie finansowe dla wybranej waluty")
    public TransactionSummary getSummary(@RequestParam @SupportedCurrency String currency) {
        return reportService.getCurrencyReport(currency);
    }
}
