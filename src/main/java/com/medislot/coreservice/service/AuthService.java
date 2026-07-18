package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.LoginRequest;
import com.medislot.coreservice.dto.request.LogoutRequest;
import com.medislot.coreservice.dto.request.RefreshTokenRequest;
import com.medislot.coreservice.dto.request.RegisterRequest;
import com.medislot.coreservice.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    void logout(LogoutRequest request);
}