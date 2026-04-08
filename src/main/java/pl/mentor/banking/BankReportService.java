package pl.mentor.banking;

import org.springframework.stereotype.Service;
import pl.mentor.banking.model.Transaction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Ta adnotacja mówi Springowi: "Zrób instancję tej klasy i trzymaj ją w swoim worku (kontenerze)"
public class BankReportService {
    public BigDecimal sumTransaction(List<Transaction> transactions, String targetCurrency){

        var sumTr = transactions
                .stream()
                .filter(t -> targetCurrency.equals(t.getCurrency()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ;

        return sumTr;
    }

    public Optional<Transaction> getLongestCurrencyNameTransaction(List<Transaction> transactions){

        return transactions.stream()
                .max(Comparator.comparingInt(t -> t.getCurrency().length()))
                ;
    }

    public Map<String, BigDecimal> sumByCurrency(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency, // 1. Po czym grupujemy (Klucz)
                        Collectors.reducing(   // 2. Co robimy z grupą (Wartość)
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                        ));
    }

    public Map<String, List<BigDecimal>> currencyList(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency, // 1. Po czym grupujemy (Klucz)
                        Collectors.mapping(Transaction::getAmount, Collectors.toList())
                ));
    }

    public Map<String, Long> countLargeTransactionsByCurrency(List<Transaction> transactions, BigDecimal threshold){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency, // 1. Po czym grupujemy (Klucz)
                        Collectors.filtering(t-> t.getAmount().compareTo(threshold)>0, Collectors.counting())
                ));
    }

    public Map<String, Optional<Transaction>>getMostExpensiveTransactionByCurrency(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency,
                        Collectors.maxBy(Comparator.comparing(Transaction::getAmount) )
                        ));
    }

}
