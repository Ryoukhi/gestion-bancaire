package com.eadl.backend.service;

import java.util.List;

import com.eadl.backend.dto.request.DepositRequest;
import com.eadl.backend.dto.request.WithdrawRequest;
import com.eadl.backend.dto.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse deposit(DepositRequest request);

    TransactionResponse withdraw(WithdrawRequest request);

    List<TransactionResponse> getAccountHistory(String accountNumber);
}