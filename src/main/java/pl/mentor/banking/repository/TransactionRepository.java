package pl.mentor.banking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCurrency(String currency);
    List<Transaction> findByAmountGreaterThan(BigDecimal amount);
    List<Transaction> findByCurrencyAndAmountGreaterThan(String currency, BigDecimal amount);
    List<Transaction> findByTimestampAfter(LocalDateTime date);
    List<Transaction> findByAmountLessThan(BigDecimal amount);
    void deleteByCurrency(String currency);

    @Query("SELECT new pl.mentor.banking.model.dto.TransactionSummary(t.currency, SUM(t.amount), COUNT(t)) " +
            "FROM Transaction t " +
            "WHERE t.currency = :currency " +
            "GROUP BY t.currency")
    Optional<TransactionSummary> getSummaryForCurrency(@Param("currency") String currency);
}