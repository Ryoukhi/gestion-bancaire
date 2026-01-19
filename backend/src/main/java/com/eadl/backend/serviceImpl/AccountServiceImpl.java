package com.eadl.backend.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eadl.backend.dto.response.AccountResponse;
import com.eadl.backend.entity.Account;
import com.eadl.backend.entity.User;
import com.eadl.backend.enums.AccountType;
import com.eadl.backend.exception.ResourceNotFoundException;
import com.eadl.backend.repository.AccountRepository;
import com.eadl.backend.repository.UserRepository;
import com.eadl.backend.service.AccountService;
import com.eadl.backend.util.AccountNumberGenerator;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountResponse createAccount(Long userId, AccountType type) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(AccountNumberGenerator.generateUUID());
        account.setType(type);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        return map(saved);
    }

    @Override
    public List<AccountResponse> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return map(account);
    }

    private AccountResponse map(Account a) {
        return new AccountResponse(
                a.getAccountNumber(),
                a.getType(),
                a.getBalance()
        );
    }
}