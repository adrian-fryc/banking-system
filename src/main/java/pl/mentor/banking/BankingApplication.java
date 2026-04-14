package pl.mentor.banking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.mentor.banking.model.dto.TransactionSummary;
import pl.mentor.banking.model.entity.Transaction;
import pl.mentor.banking.model.entity.User;
import pl.mentor.banking.repository.TransactionRepository;
import pl.mentor.banking.repository.UserRepository;

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
    public CommandLineRunner seedData(TransactionRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
//            Na czas nauki kasuję zawsze wszystko z bazy i na nowo uzupełniam
            repository.deleteAll();
            userRepository.deleteAll();

            if (repository.count()==0) {
                log.info("Baza jest pusta. Generowanie danych startowych");
                User jan = new User("Jan", "Kowalski", new BigDecimal("20000.00"));
                jan.setUsername("jan123");
                jan.setPassword(passwordEncoder.encode("pancernyJan")); // Szyfrujemy!
                jan.setRole("USER");
//                var t1 = new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now());

                jan.addTransaction(new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()));
                jan.addTransaction(new Transaction(new BigDecimal("50.00"), "USD", LocalDateTime.now()));

                User anna = new User("Anna", "Nowak", new BigDecimal("10000.00"));
//                var t2 = new Transaction(new BigDecimal("60.00"), "PLN", LocalDateTime.now());
                anna.addTransaction(new Transaction(new BigDecimal("250.00"), "PLN", LocalDateTime.now()));
                anna.addTransaction(new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()));
                var users = List.of(
                        anna,
                        jan
                );
                userRepository.saveAll(users);
//                var transactions = List.of(
//                        new Transaction(new BigDecimal("100.00"), "PLN", LocalDateTime.now()),
//                        new Transaction(new BigDecimal("50.00"), "PLN", LocalDateTime.now()),
//                        new Transaction(new BigDecimal("99.99"), "USD", LocalDateTime.now())
//                );
//                repository.saveAll(transactions);


                log.info("Baza została zasilona przykładowymi danymi!");
            }else{
                log.info("Dane już istnieją w bazie, pomijam inicjalizację.");
            }
        };
    }

}
