package com.eadl.backend.service;

import java.util.List;

import com.eadl.backend.dto.response.AccountResponse;
import com.eadl.backend.enums.AccountType;

public interface AccountService {

    AccountResponse createAccount(Long userId, AccountType type);

    List<AccountResponse> getUserAccounts(Long userId);

    AccountResponse getAccountByNumber(String accountNumber);
}