package com.reconix.auth.service;

import com.reconix.auth.domain.dto.AuthResponse;
import com.reconix.auth.domain.dto.LoginRequest;
import com.reconix.auth.domain.dto.RegisterRequest;
import com.reconix.auth.domain.entity.Tenant;
import com.reconix.auth.domain.entity.User;
import com.reconix.auth.domain.enums.Role;
import com.reconix.auth.repository.TenantRepository;
import com.reconix.auth.repository.UserRepository;
//import com.reconix.auth.security.JwtTokenProvider; // Vamos descomentar depois
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder; // Vamos descomentar depois
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    //private final PasswordEncoder passwordEncoder;
    //private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email ja cadastrado: " + request.email());
        }

        Tenant tenant = tenantRepository.findBySlug(request.tenantSlug())
                .orElseThrow(() -> new RuntimeException("Tenant nao encontrado: " + request.tenantSlug()));

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                //.password(passwordEncoder.encode(request.password()))
                .password(request.password()) // Temporario, sem criptografia por enquanto
                .role(Role.ANALYST)
                .tenant(tenant)
                .build();

        User saved = userRepository.save(user);
        return generateAuthResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciais invalidas."));

        //if (!passwordEncoder.matches(request.password(), user.getPassword())) {
        if (!user.getPassword().equals(request.password())) { // Temporario, sem criptografia
            throw new RuntimeException("Credenciais invalidas.");
        }

        if (!user.getActive()) {
            throw new RuntimeException("Usuario desativado.");
        }

        return generateAuthResponse(user);
    }

    private AuthResponse generateAuthResponse(User user) {
        // String accessToken = jwtTokenProvider.generateAccessToken(user);
        // String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        // Mockando os tokens ate implementarmos a camada JWT
        String accessToken = "DUMMY_TOKEN";
        String refreshToken = "DUMMY_REFRESH_TOKEN";

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                3600L,
                user.getTenant().getSlug(),
                user.getRole().name()
        );
    }
}
