package pl.mentor.banking.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.InitBinder;
import pl.mentor.banking.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor // Lombok wygeneruje pusty konstruktor dla JPA
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    private String firsName;

    @NotNull
    private String lastName;

    private BigDecimal balance;

    @Column(unique = true)
    private String username;
    private String password;
    private String role; // np. "USER" lub "ADMIN"

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();


    public User(String firsName, String lastName ) {
        this.firsName = firsName;
        this.lastName = lastName;
    }

    public User(String firsName, String lastName, BigDecimal balance ) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.balance = (balance == null) ? BigDecimal.ZERO : balance;
    }

    public User(String firsName, String lastName, List<Transaction> transactions ) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.transactions = transactions;
    }

    public User(String firsName, String lastName, List<Transaction> transactions, BigDecimal balance ) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.transactions = transactions;
        this.balance = (balance == null) ? BigDecimal.ZERO : balance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setUser(this); // To jest kluczowe! Ustawiamy relację w obie strony
    }

    public void debit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kwota obciążenia musi być dodatnia");
        }

        // Sprawdzamy czy starczy pieniędzy
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Masz za mało kasy! Brakuje: " + amount.subtract(this.balance));
        }

        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kwota wpłaty musi być dodatnia");
        }
        this.balance = this.balance.add(amount);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
