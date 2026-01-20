package com.eadl.backend.service;

import com.eadl.backend.dto.response.AccountResponse;
import com.eadl.backend.entity.Account;
import com.eadl.backend.entity.User;
import com.eadl.backend.enums.AccountType;
import com.eadl.backend.exception.ResourceNotFoundException;
import com.eadl.backend.repository.AccountRepository;
import com.eadl.backend.repository.UserRepository;
import com.eadl.backend.serviceImpl.AccountServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User testUser;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setFullName("testuser");
        testUser.setEmail("test@example.com");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC-123456789");
        testAccount.setType(AccountType.EPARGNE);
        testAccount.setBalance(BigDecimal.valueOf(1000.00));
        testAccount.setUser(testUser);
    }

    @Test
    void createAccount_ShouldCreateAccountSuccessfully_WhenUserExists() {
        // Given
        Long userId = 1L;
        AccountType accountType = AccountType.EPARGNE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse result = accountService.createAccount(userId, accountType);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountNumber()).isEqualTo(testAccount.getAccountNumber());
        assertThat(result.getType()).isEqualTo(accountType);
        assertThat(result.getBalance()).isEqualTo(testAccount.getBalance());

        verify(userRepository, times(1)).findById(userId);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        // Given
        Long userId = 999L;
        AccountType accountType = AccountType.COURANT;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.createAccount(userId, accountType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findById(userId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void createAccount_ShouldCreateAccountWithZeroBalance() {
        // Given
        Long userId = 1L;
        AccountType accountType = AccountType.COURANT;

        Account savedAccount = new Account();
        savedAccount.setAccountNumber("ACC-987654321");
        savedAccount.setType(accountType);
        savedAccount.setBalance(BigDecimal.ZERO);
        savedAccount.setUser(testUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // When
        AccountResponse result = accountService.createAccount(userId, accountType);

        // Then
        assertThat(result.getBalance()).isEqualTo(BigDecimal.ZERO);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void getUserAccounts_ShouldReturnListOfAccounts_WhenUserHasAccounts() {
        // Given
        Long userId = 1L;
        
        Account account1 = new Account();
        account1.setAccountNumber("ACC-111111111");
        account1.setType(AccountType.EPARGNE);
        account1.setBalance(BigDecimal.valueOf(1000.00));

        Account account2 = new Account();
        account2.setAccountNumber("ACC-222222222");
        account2.setType(AccountType.COURANT);
        account2.setBalance(BigDecimal.valueOf(500.00));

        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountRepository.findByUserId(userId)).thenReturn(accounts);

        // When
        List<AccountResponse> result = accountService.getUserAccounts(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAccountNumber()).isEqualTo("ACC-111111111");
        assertThat(result.get(0).getType()).isEqualTo(AccountType.EPARGNE);
        assertThat(result.get(1).getAccountNumber()).isEqualTo("ACC-222222222");
        assertThat(result.get(1).getType()).isEqualTo(AccountType.COURANT);

        verify(accountRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getUserAccounts_ShouldReturnEmptyList_WhenUserHasNoAccounts() {
        // Given
        Long userId = 1L;

        when(accountRepository.findByUserId(userId)).thenReturn(List.of());

        // When
        List<AccountResponse> result = accountService.getUserAccounts(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(accountRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAccountByNumber_ShouldReturnAccount_WhenAccountExists() {
        // Given
        String accountNumber = "ACC-123456789";

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(testAccount));

        // When
        AccountResponse result = accountService.getAccountByNumber(accountNumber);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(result.getType()).isEqualTo(testAccount.getType());
        assertThat(result.getBalance()).isEqualTo(testAccount.getBalance());

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void getAccountByNumber_ShouldThrowResourceNotFoundException_WhenAccountDoesNotExist() {
        // Given
        String accountNumber = "ACC-NONEXISTENT";

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.getAccountByNumber(accountNumber))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found");

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void createAccount_ShouldGenerateUniqueAccountNumber() {
        // Given
        Long userId = 1L;
        AccountType accountType = AccountType.EPARGNE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse result = accountService.createAccount(userId, accountType);

        // Then
        assertThat(result.getAccountNumber()).isNotNull();
        assertThat(result.getAccountNumber()).isNotEmpty();
        
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void getUserAccounts_ShouldMapAllFieldsCorrectly() {
        // Given
        Long userId = 1L;
        List<Account> accounts = Arrays.asList(testAccount);

        when(accountRepository.findByUserId(userId)).thenReturn(accounts);

        // When
        List<AccountResponse> result = accountService.getUserAccounts(userId);

        // Then
        assertThat(result).hasSize(1);
        AccountResponse response = result.get(0);
        assertThat(response.getAccountNumber()).isEqualTo(testAccount.getAccountNumber());
        assertThat(response.getType()).isEqualTo(testAccount.getType());
        assertThat(response.getBalance()).isEqualTo(testAccount.getBalance());
    }
}