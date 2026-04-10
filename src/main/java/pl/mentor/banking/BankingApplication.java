package pl.mentor.banking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.model.entity.Transaction;
import pl.mentor.banking.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication // To jest "magiczny przełącznik" - mówi Springowi: "Hej, zacznij skanować projekt!"
@Slf4j
public class BankingApplication {

    public static void main(String[] args) {
        // Ta linijka odpala cały silnik Spring Boot i wbudowany serwer WWW
        SpringApplication.run(BankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedData(TransactionRepository repository) {
        return args -> {
            if (repository.count()==0) {
                log.info("Baza jest pusta. Generowanie danych startowych");
                var transactions = List.of(
                        new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
                        new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()),
                        new Transaction(new BigDecimal("99.99"), "USD", LocalDateTime.now())
                );
                repository.saveAll(transactions);
                log.info("Baza została zasilona przykładowymi danymi!");
            }else{
                log.info("Dane już istnieją w bazie, pomijam inicjalizację.");
            }
        };
    }

}
