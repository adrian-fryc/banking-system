package pl.mentor.banking;

import java.math.BigDecimal;
import java.util.List;

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
}
