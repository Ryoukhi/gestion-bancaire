package com.eadl.backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

import com.eadl.backend.enums.AccountType;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class AccountResponse {

    private String accountNumber;
    private AccountType type;
    private BigDecimal balance;
}
