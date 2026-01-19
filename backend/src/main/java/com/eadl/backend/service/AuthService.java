package com.eadl.backend.service;

import com.eadl.backend.dto.request.LoginRequest;
import com.eadl.backend.dto.request.RegisterRequest;
import com.eadl.backend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    String register(RegisterRequest request);
}