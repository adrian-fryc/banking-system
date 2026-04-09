package pl.mentor.banking.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    @Size(min=3, max=3)
    private String currency;

    @NotNull
    @PastOrPresent
    private LocalDateTime timestamp;

    // 1. Pusty konstruktor - BEZWZGLĘDNIE WYMAGANY przez JPA
    public Transaction() {}

    // 2. Konstruktor dla Ciebie (do tworzenia obiektów)
    public Transaction(BigDecimal amount, String currency, LocalDateTime timestamp) {
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
    }

    // 3. Gettery i Settery (Musisz je mieć! Użyj Alt+Insert w IntelliJ -> Getter and Setter)
    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}