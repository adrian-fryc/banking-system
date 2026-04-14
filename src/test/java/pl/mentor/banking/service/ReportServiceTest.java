package pl.mentor.banking.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mentor.banking.exception.CurrencyNotFoundException;
import pl.mentor.banking.exception.UserNotFoundException;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.model.entity.Transaction;
import pl.mentor.banking.model.entity.User;
import pl.mentor.banking.repository.TransactionRepository;
import pl.mentor.banking.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportService reportService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NbpService nbpService;

    @Test
    void shouldReturnSummaryForCurrencyExists(){
        String currency = "PLN";
        User adam = new User("Adam", "Kowalski");
        adam.addTransaction(new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()));
        adam.addTransaction(new Transaction(new BigDecimal("10.00"), "PLN", LocalDateTime.now()));
        adam.addTransaction(new Transaction(new BigDecimal("200.00"), "USD", LocalDateTime.now()));

        List<Transaction> transactions = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                new Transaction(new BigDecimal("10.00"), "USD", LocalDateTime.now()),
                new Transaction(new BigDecimal("200.00"), "USD", LocalDateTime.now())
        );

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(adam));

        Mockito.when(transactionRepository.findByUserId(anyLong())).thenReturn(transactions);
        Mockito.when(nbpService.getExchangeRate("PLN")).thenReturn(BigDecimal.ONE);
        Mockito.when(nbpService.getExchangeRate("USD")).thenReturn(new BigDecimal("2.00"));

        TransactionSummary result = reportService.getUserCurrencyReport(1L, "PLN");
        assertThat(result.getAmount()).isEqualByComparingTo("520.0");
//        assertEquals(new BigDecimal("520.00"), result.getAmount());

    }

    @Test
    void shouldReturnUserNotFoundException(){
        String currency = "PLN";
        User adam = new User("Adam", "Kowalski");

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->{
            reportService.getUserCurrencyReport(0L, "PLN");
        });
        assertTrue(exception.getMessage().contains("Bank nie posiada użytkownika o ID"));
    }

    @Test
    void shouldReturnCurrencyNotFoundException(){
        String currency = "ABC";
        User adam = new User("Adam", "Kowalski"); // Tworzymy obiekt usera
        List<Transaction> transactions = List.of(
                new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now())
        );

        // 1. Musimy udawać, że user istnieje (to jest ten brakujący element!)
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(adam));

        Mockito.when(transactionRepository.findByUserId(anyLong())).thenReturn(transactions);

        // Musisz dodać to, bo Twoja transakcja jest w PLN
        Mockito.when(nbpService.getExchangeRate("PLN")).thenReturn(BigDecimal.ONE);

        // 2. Udajemy, że NBP nie zna waluty (zwracamy null)
//        Mockito.when(nbpService.getExchangeRate(currency)).thenReturn(null);
        // Zamiast .thenReturn(null), używamy .thenThrow
        Mockito.when(nbpService.getExchangeRate(currency))
                .thenThrow(new CurrencyNotFoundException("Bank nie posiada transakcji w walucie: " + currency));

        // WHEN & THEN
        CurrencyNotFoundException exception = assertThrows(CurrencyNotFoundException.class, () ->{
            reportService.getUserCurrencyReport(34L, currency);
        });
        assertTrue(exception.getMessage().contains("Bank nie posiada transakcji w walucie"));
    }


}
