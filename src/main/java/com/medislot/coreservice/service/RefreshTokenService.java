package com.medislot.coreservice.service;

import com.medislot.coreservice.entity.RefreshToken;
import com.medislot.coreservice.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void revokeToken(String token);
}