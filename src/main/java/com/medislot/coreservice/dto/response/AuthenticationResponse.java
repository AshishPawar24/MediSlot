// dto/response/AuthenticationResponse.java
package com.medislot.coreservice.dto.response;

import com.medislot.coreservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String email;
    private Role role;
}