package pl.mentor.banking.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.mentor.banking.exception.CurrencyNotFoundException;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.model.entity.Transaction;
import pl.mentor.banking.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportService {

    // To jest nasz "pilot" do bazy danych
    private final TransactionRepository transactionRepository;

    // Spring sam znajdzie TransactionRepository i wstawi je tutaj
    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // BankReportService

    public BigDecimal sumTransaction(List<Transaction> transactions, String targetCurrency){

        var sumTr = transactions
                .stream()
                .filter(t -> targetCurrency.equals(t.getCurrency()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ;

        return sumTr;
    }

    public Optional<Transaction> getLongestCurrencyNameTransaction(List<Transaction> transactions){

        return transactions.stream()
                .max(Comparator.comparingInt(t -> t.getCurrency().length()))
                ;
    }

    public Map<String, BigDecimal> sumByCurrency(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency, // 1. Po czym grupujemy (Klucz)
                        Collectors.reducing(   // 2. Co robimy z grupą (Wartość)
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    public Map<String, List<BigDecimal>> currencyList(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency, // 1. Po czym grupujemy (Klucz)
                        Collectors.mapping(Transaction::getAmount, Collectors.toList())
                ));
    }

    public Map<String, Long> countLargeTransactionsByCurrency(List<Transaction> transactions, BigDecimal threshold){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency, // 1. Po czym grupujemy (Klucz)
                        Collectors.filtering(t-> t.getAmount().compareTo(threshold)>0, Collectors.counting())
                ));
    }

    public Map<String, Optional<Transaction>>getMostExpensiveTransactionByCurrency(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency,
                        Collectors.maxBy(Comparator.comparing(Transaction::getAmount) )
                ));
    }

    // END OF BankReportService

    public Map<String, BigDecimal> calculateAndSave(List<Transaction> transactions) {
        // KROK 1: Zapisujemy wszystko do Postgresa (magia Hibernate!)
        transactionRepository.saveAll(transactions);

        // KROK 2: Twoja stara logika sumowania (Streamy)
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> findByCurrency(String currency){
        return transactionRepository.findByCurrency(currency);
    }

    public List<Transaction> findByAmountGreaterThan(BigDecimal amount){
        return transactionRepository.findByAmountGreaterThan(amount);
    }

    public List<Transaction> findByCurrencyAndAmountGreaterThan(String currency, BigDecimal amount){
        return transactionRepository.findByCurrencyAndAmountGreaterThan(currency, amount);
    }

    public List<Transaction> findByTimestampAfter(LocalDateTime date){
        return transactionRepository.findByTimestampAfter(date);
    }

    public List<Transaction> findByAmountLessThan(BigDecimal amount){
        return transactionRepository.findByAmountLessThan(amount);
    }

    @Transactional
    public void deleteByCurrency(String currency){
        transactionRepository.deleteByCurrency(currency);
    }

    @Transactional
    public void updateAmount(Long id, BigDecimal newAmount){
        log.info("Próba aktualizacji kwoty dla ID: {} na wartość: {}", id, newAmount);
        Transaction record = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Nie znaleziono transakcji o ID: {}", id);
                    return new EntityNotFoundException("Transakcja o ID " + id + " nie istnieje!");
                });

        record.setAmount(newAmount);
        log.info("Pomyślnie zaktualizowano transakcję o ID: {}", id);
    }

    public TransactionSummary getCurrencyReport(String currency) {
        log.info("Rozpoczynam generowanie raportu dla waluty: {}", currency);

        return transactionRepository.getSummaryForCurrency(currency)
                .orElseThrow(() -> {
                    log.warn("Baza danych nie zwróciła wyników dla waluty: {}", currency);
                    return new CurrencyNotFoundException(currency);
                });
    }

}