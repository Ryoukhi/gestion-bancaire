package com.eadl.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.eadl.backend.dto.request.DepositRequest;
import com.eadl.backend.dto.request.WithdrawRequest;
import com.eadl.backend.dto.response.TransactionResponse;
import com.eadl.backend.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Gestion des dépôts, retraits et historique")
@SecurityRequirement(name = "BearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Dépôt d'argent",
            description = "Effectue un dépôt sur un compte bancaire")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dépôt effectué avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    @PostMapping("/deposit")
    public TransactionResponse deposit(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Informations du dépôt")
            @RequestBody DepositRequest request) {

        return transactionService.deposit(request);
    }

    @Operation(summary = "Retrait d'argent",
            description = "Effectue un retrait sur un compte bancaire")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrait effectué avec succès"),
            @ApiResponse(responseCode = "400", description = "Solde insuffisant"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Informations du retrait")
            @RequestBody WithdrawRequest request) {

        return transactionService.withdraw(request);
    }

    @Operation(summary = "Historique des transactions",
            description = "Retourne l'historique des transactions d'un compte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historique retourné"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    @GetMapping("/history/{accountNumber}")
    public List<TransactionResponse> history(
            @Parameter(description = "Numéro du compte", example = "ACC123456789")
            @PathVariable String accountNumber) {

        return transactionService.getAccountHistory(accountNumber);
    }
}