package org.nuist.service.impl;

import io.jsonwebtoken.JwtException;
import org.nuist.dto.response.LoginResponseDto;
import org.nuist.entity.TokenResponse;
import org.nuist.model.Role;
import org.nuist.model.User;
import org.nuist.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.nuist.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseDto login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = (User) authentication.getPrincipal();
//        return jwtUtil.generateToken(user.getUsername());
        return LoginResponseDto.builder()
                .tokens(jwtUtil.generateToken(user.getUsername()))
                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                .build();
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new JwtException("Invalid JWT refresh token");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        return jwtUtil.generateToken(username);
    }
}
