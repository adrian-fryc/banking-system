package pl.mentor.banking;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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


}
