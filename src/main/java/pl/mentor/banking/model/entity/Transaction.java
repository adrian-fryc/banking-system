package pl.mentor.banking.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Setter;
import pl.mentor.banking.validation.SupportedCurrency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
//@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Kwota nie może być pusta")
//    @Positive(message = "Kwota transakcji musi być dodatnia")
    private BigDecimal amount;

    @NotBlank
    @Size(min=3, max=3)
    @SupportedCurrency
    private String currency;

    @NotNull
    @PastOrPresent
    private LocalDateTime timestamp;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    public User getUser() {
        return user;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}