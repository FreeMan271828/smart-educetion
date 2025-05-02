package org.nuist.service;

import org.nuist.entity.TokenResponse;

public interface AuthenticationService {
    TokenResponse login(String username, String password);
}
