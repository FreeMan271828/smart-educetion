package org.nuist.service;

import org.nuist.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse login(String username, String password);
}
