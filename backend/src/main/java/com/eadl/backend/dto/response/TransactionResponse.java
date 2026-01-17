package com.eadl.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.eadl.backend.enums.TransactionType;

import lombok.*;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class TransactionResponse {

    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime date;
    private BigDecimal balanceAfter;
}