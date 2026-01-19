package com.eadl.backend.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.backend.dto.request.DepositRequest;
import com.eadl.backend.dto.request.WithdrawRequest;
import com.eadl.backend.dto.response.TransactionResponse;
import com.eadl.backend.entity.Account;
import com.eadl.backend.entity.Transaction;
import com.eadl.backend.enums.TransactionType;
import com.eadl.backend.exception.BusinessException;
import com.eadl.backend.exception.ResourceNotFoundException;
import com.eadl.backend.repository.AccountRepository;
import com.eadl.backend.repository.TransactionRepository;
import com.eadl.backend.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository,
                                  TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        Account account = getAccount(request.getAccountNumber());

        account.setBalance(account.getBalance().add(request.getAmount()));

        Transaction tx = buildTransaction(account, request.getAmount(), TransactionType.DEPOT);

        transactionRepository.save(tx);
        accountRepository.save(account);

        return map(tx);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        Account account = getAccount(request.getAccountNumber());

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction tx = buildTransaction(account, request.getAmount(), TransactionType.RETRAIT);

        transactionRepository.save(tx);
        accountRepository.save(account);

        return map(tx);
    }

    @Override
    public List<TransactionResponse> getAccountHistory(String accountNumber) {

        Account account = getAccount(accountNumber);

        return transactionRepository
                .findByAccountIdOrderByDateDesc(account.getId())
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private Transaction buildTransaction(Account account, 
                                         java.math.BigDecimal amount, 
                                         TransactionType type) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setDate(LocalDateTime.now());
        tx.setBalanceAfter(account.getBalance());
        return tx;
    }

    private TransactionResponse map(Transaction t) {
        return new TransactionResponse(
                t.getType(),
                t.getAmount(),
                t.getDate(),
                t.getBalanceAfter()
        );
    }
}