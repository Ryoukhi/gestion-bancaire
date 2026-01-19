package com.eadl.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.eadl.backend.dto.response.AccountResponse;
import com.eadl.backend.enums.AccountType;
import com.eadl.backend.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Gestion des comptes bancaires")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Créer un compte",
            description = "Crée un compte bancaire pour un utilisateur donné")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compte créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/create")
    public AccountResponse createAccount(
            @Parameter(description = "ID de l'utilisateur", example = "1")
            @RequestParam Long userId,

            @Parameter(description = "Type de compte", example = "SAVINGS")
            @RequestParam AccountType type) {

        return accountService.createAccount(userId, type);
    }

    @Operation(summary = "Lister les comptes d'un utilisateur",
            description = "Retourne tous les comptes associés à un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des comptes retournée"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/user/{userId}")
    public List<AccountResponse> getUserAccounts(
            @Parameter(description = "ID utilisateur", example = "1")
            @PathVariable Long userId) {

        return accountService.getUserAccounts(userId);
    }

    @Operation(summary = "Consulter un compte",
            description = "Retourne les informations d’un compte via son numéro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compte trouvé"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(
            @Parameter(description = "Numéro du compte", example = "ACC123456789")
            @PathVariable String accountNumber) {

        return accountService.getAccountByNumber(accountNumber);
    }
}