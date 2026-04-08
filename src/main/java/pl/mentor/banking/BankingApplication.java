package pl.mentor.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // To jest "magiczny przełącznik" - mówi Springowi: "Hej, zacznij skanować projekt!"
public class BankingApplication {

    public static void main(String[] args) {
        // Ta linijka odpala cały silnik Spring Boot i wbudowany serwer WWW
        SpringApplication.run(BankingApplication.class, args);
    }

}
