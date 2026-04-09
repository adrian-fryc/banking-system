package pl.mentor.banking;

import org.springframework.web.bind.annotation.*;
import pl.mentor.banking.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController // Mówi Springowi: "Ta klasa obsługuje zapytania HTTP i zwraca dane (zwykle JSON)"
@RequestMapping("/api/reports") // Wszystkie adresy w tej klasie będą zaczynać się od /api/reports
public class BankReportController {

    private final BankReportService bankReportService;
    private final ReportService reportService;


    public BankReportController(BankReportService bankReportService, ReportService reportService) {
        this.bankReportService = bankReportService;
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
        return bankReportService.sumByCurrency(mockTransactions);
    }

    @GetMapping("/single")
    public TransactionRecord getSingle() {
        return new TransactionRecord(new BigDecimal("100.00"), "PLN", LocalDateTime.now());
    }

    @PostMapping("/calculate")
    public Map<String, BigDecimal> calculateSum(@RequestBody List<Transaction> transactions) {
        // Spring bierze JSON-a z Twojego zapytania i zamienia go na listę obiektów Transaction
        return reportService.calculateAndSave(transactions);
    }

    @GetMapping("/all")
    public List<Transaction> getHistory(){
        return reportService.getAllTransactions();
    }

    @GetMapping("/filter")
    public List<Transaction> getFilteredHistory(@RequestParam String currency){
        return reportService.findByCurrency(currency);
    }

    @GetMapping("/grater")
    public List<Transaction> getAmountGreaterThan(@RequestParam BigDecimal amount){
        return reportService.findByAmountGreaterThan(amount);
    }

    @GetMapping("/greaterCurrency")
    public List<Transaction> getCurrencyAndAmountGreaterThan(@RequestParam String currency, @RequestParam(required = false) BigDecimal amount){
        return reportService.findByCurrencyAndAmountGreaterThan(currency, amount);
    }

    @GetMapping("/date")
    public List<Transaction> getHistoryAfterDate(@RequestParam LocalDateTime date){
        return reportService.findByTimestampAfter(date);
    }

    @GetMapping("/less")
    public List<Transaction> getAmountLessThan(@RequestParam BigDecimal amount){
        return reportService.findByAmountLessThan(amount);
    }

    @DeleteMapping("/deleteCurrency/{currency}")
    public void deleteByCurrency(@PathVariable String currency){
        reportService.deleteByCurrency(currency);
    }

    @PutMapping("/update/{id}")
    public void setAmount(@PathVariable  Long id, @RequestParam BigDecimal newAmount){
        reportService.updateAmount(id, newAmount);
    }
}
