package pl.mentor.banking;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankReportService {
    public BigDecimal sumTransaction(List<Transaction> transactions, String targetCurrency){

        var sumTr = transactions
                .stream()
                .filter(t -> targetCurrency.equals(t.currency()))
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ;

        return sumTr;
    }

    public Optional<Transaction> getLongestCurrencyNameTransaction(List<Transaction> transactions){

        return transactions.stream()
                .max(Comparator.comparingInt(t -> t.currency().length()))
                ;
    }

    public Map<String, BigDecimal> sumByCurrency(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::currency, // 1. Po czym grupujemy (Klucz)
                        Collectors.reducing(   // 2. Co robimy z grupą (Wartość)
                                BigDecimal.ZERO,
                                Transaction::amount,
                                BigDecimal::add
                        )
                        ));
    }

    public Map<String, List<BigDecimal>> currencyList(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::currency, // 1. Po czym grupujemy (Klucz)
                        Collectors.mapping(Transaction::amount, Collectors.toList())
                ));
    }

    public Map<String, Long> countLargeTransactionsByCurrency(List<Transaction> transactions, BigDecimal threshold){
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::currency, // 1. Po czym grupujemy (Klucz)
                        Collectors.filtering(t-> t.amount().compareTo(threshold)>0, Collectors.counting())
                ));
    }

    public Map<String, Optional<Transaction>>getMostExpensiveTransactionByCurrency(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::currency,
                        Collectors.maxBy(Comparator.comparing(Transaction::amount) )
                        ));
    }

}
