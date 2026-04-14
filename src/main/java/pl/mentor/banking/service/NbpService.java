package pl.mentor.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mentor.banking.model.dto.NbpResponse;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class NbpService {
    private final RestTemplate restTemplate;// = new RestTemplate();
    private final String NBP_URL = "http://api.nbp.pl/api/exchangerates/rates/a/{code}/?format=json";

    public BigDecimal getExchangeRate(String currencyCode){
        if("PLN".equalsIgnoreCase(currencyCode)) return BigDecimal.ONE;

        try {
            NbpResponse response = restTemplate.getForObject(NBP_URL, NbpResponse.class, currencyCode);

            // 1. Sprawdzamy czy cała odpowiedź nie jest nullem
            // 2. Sprawdzamy czy lista rates istnieje i czy ma elementy
            if (response != null && response.rates() != null && !response.rates().isEmpty()) {
                return response.rates().get(0).mid();
            }

            throw new RuntimeException("NBP zwrócił pustą listę kursów dla: " + currencyCode);

        } catch (Exception e) {
            // Tu wpadną błędy 404 (brak waluty), 500 (NBP leży) lub problemy z siecią
            throw new RuntimeException("Nie udało się pobrać kursu waluty " + currencyCode + ": " + e.getMessage());
        }
    }
}
