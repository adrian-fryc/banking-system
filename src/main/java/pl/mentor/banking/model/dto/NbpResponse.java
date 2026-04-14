package pl.mentor.banking.model.dto;

import java.util.List;

public record NbpResponse(
        String table,
        String currency,
        String code,
        List<RateDTO> rates
) {}
