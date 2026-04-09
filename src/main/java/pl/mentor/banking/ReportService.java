package pl.mentor.banking;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.mentor.banking.model.Transaction;
import pl.mentor.banking.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    // To jest nasz "pilot" do bazy danych
    private final TransactionRepository transactionRepository;

    // Spring sam znajdzie TransactionRepository i wstawi je tutaj
    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

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

}