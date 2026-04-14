package pl.mentor.banking.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mentor.banking.model.entity.Transaction;
import pl.mentor.banking.repository.TransactionRepository;
import pl.mentor.banking.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TransferService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount){

        if(Objects.equals(fromUserId, toUserId)){
            throw new RuntimeException("Przelew sam do siebie zablokowany");
        }

        var fromUser = userRepository.findById(fromUserId).orElseThrow(() -> {
            System.out.println("Nie znaleziono w bazie: " + fromUserId);
            return new UsernameNotFoundException("User not found");
        });

        fromUser.debit(amount);
        var toUser = userRepository.findById(toUserId).orElseThrow(() -> {
            System.out.println("Nie znaleziono w bazie: " + toUserId);
            return new UsernameNotFoundException("User not found");
        });
        toUser.credit(amount);

        // Wewnątrz metody transfer, po fromUser.debit(amount) i toUser.credit(amount):

// 1. Transakcja wychodząca dla nadawcy
        Transaction outgoing = new Transaction(
                amount.negate(), // Kwota ujemna (np. -100)
                "PLN",           // Tu możesz wyciągnąć walutę z konta usera
                LocalDateTime.now()
        );
        outgoing.setUser(fromUser); // Powiązanie z nadawcą
        outgoing.setDescription("Przelew do: " + toUser.getFirsName() + " " + toUser.getLastName());

// 2. Transakcja przychodząca dla odbiorcy
        Transaction incoming = new Transaction(
                amount,          // Kwota dodatnia (np. 100)
                "PLN",
                LocalDateTime.now()
        );
        incoming.setUser(toUser);   // Powiązanie z odbiorcą
        incoming.setDescription("Przelew od: " + fromUser.getFirsName() + " " + fromUser.getLastName());

// 3. Zapis do bazy
        transactionRepository.saveAll(List.of(outgoing, incoming));

        userRepository.saveAll(List.of(fromUser, toUser));
    }
}
