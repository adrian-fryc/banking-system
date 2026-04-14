package pl.mentor.banking.model.dto;

import java.math.BigDecimal;

public record RateDTO(
   String no,
   String eddectiveDate,
   BigDecimal mid
) {}
