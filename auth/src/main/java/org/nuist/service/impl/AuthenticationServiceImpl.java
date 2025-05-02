package org.nuist.service.impl;

import org.nuist.entity.TokenResponse;
import org.nuist.model.User;
import org.nuist.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.nuist.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Override
    public TokenResponse login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = (User) authentication.getPrincipal();
        return jwtUtil.generateToken(user.getUsername());
    }
}
