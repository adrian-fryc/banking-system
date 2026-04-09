package pl.mentor.banking.repository;

import pl.mentor.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCurrency(String currency);
    List<Transaction> findByAmountGreaterThan(BigDecimal amount);
    List<Transaction> findByCurrencyAndAmountGreaterThan(String currency, BigDecimal amount);
    List<Transaction> findByTimestampAfter(LocalDateTime date);
    List<Transaction> findByAmountLessThan(BigDecimal amount);
    void deleteByCurrency(String currency);
}