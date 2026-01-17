package com.eadl.backend.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DepositRequest {

    private String accountNumber;
    private BigDecimal amount;
}