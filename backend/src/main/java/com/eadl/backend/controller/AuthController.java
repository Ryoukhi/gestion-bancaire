package com.eadl.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.eadl.backend.dto.request.LoginRequest;
import com.eadl.backend.dto.request.RegisterRequest;
import com.eadl.backend.dto.response.AuthResponse;
import com.eadl.backend.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Gestion de l'authentification et de l'inscription")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Connexion utilisateur",
            description = "Authentifie un utilisateur et retourne un JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }

    @Operation(summary = "Inscription utilisateur",
            description = "Crée un nouveau compte utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public String register(
            
            @RequestBody RegisterRequest request) {

        return authService.register(request);
    }
}