package pl.mentor.banking.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import pl.mentor.banking.model.entity.User;
import pl.mentor.banking.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class TransferServiceIntegrationTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldTransferMoney(){
        // GIVEN
        var adam = new User("Adam", "Kowalski", new BigDecimal("1000.00"));
        var ewa = new User("Ewa", "Nowak", new BigDecimal("500.00"));

        // Zapisujemy i odbieramy obiekty z nadanymi ID
//        var savedUsers = userRepository.saveAll(List.of(adam, ewa));
//        Long adamId = savedUsers.get(0).getId();
//        Long ewaId = savedUsers.get(1).getId();
        User savedAdam = userRepository.save(adam);
        User savedEwa = userRepository.save(ewa);

        // WHEN
        transferService.transfer(savedAdam.getId(), savedEwa.getId(), new BigDecimal("100.00"));

        // THEN
        User adamAfter = userRepository.findById(savedAdam.getId()).orElseThrow();
        User ewaAfter = userRepository.findById(savedEwa.getId()).orElseThrow();

        // Sprawdź czy saldo się zgadza (pamiętaj o skali BigDecimal!)
        assertEquals(0, new BigDecimal("900.00").compareTo(adamAfter.getBalance()));
        assertEquals(0, new BigDecimal("600.00").compareTo(ewaAfter.getBalance()));
    }

    @Test
    void shouldRollbackWhenRecipientDoesNotExist() {
        // 1. GIVEN: Adam ma 1000 PLN, a Ewy nie ma w bazie (lub używamy błędnego ID)
        User adam = new User("Adam", "Kowalski", new BigDecimal("1000.00"));
        User savedAdam = userRepository.save(adam);
        Long nonExistentId = -99L; // To ID na pewno nie istnieje

        // 2. WHEN: Próbujemy zrobić przelew do nieistniejącego odbiorcy
        assertThrows(UsernameNotFoundException.class, () -> {
            transferService.transfer(savedAdam.getId(), nonExistentId, new BigDecimal("100.00"));
        });

        // 3. THEN: Kluczowy moment!
        // Pobieramy Adama z bazy ponownie, żeby zobaczyć jego stan po nieudanym przelewie
        User adamAfter = userRepository.findById(savedAdam.getId()).orElseThrow();
        log.info("adamAfter getBalance: " + adamAfter.getBalance());
        assertEquals(0, new BigDecimal("1000.00").compareTo(adamAfter.getBalance()), "Pieniądze powinny wrócić na konto Adama!");
    }

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

}
