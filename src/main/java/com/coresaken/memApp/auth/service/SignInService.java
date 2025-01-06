package com.coresaken.memApp.auth.service;

import com.coresaken.memApp.auth.dto.request.SignInRequestDto;
import com.coresaken.memApp.auth.dto.response.TokenResponse;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.repository.auth.ActiveAccountTokenRepository;
import com.coresaken.memApp.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final ActiveAccountTokenRepository activeAccountTokenRepository;

    public ResponseEntity<TokenResponse> signIn(SignInRequestDto request) {
        String identifier = request.identifier();

        User user = userRepository.findByEmailOrLogin(identifier, identifier).orElse(null);
        if (user == null) {
            return TokenResponse.badRequestToken(1, "Niepoprawne dane logowania!");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return TokenResponse.badRequestToken(2, "Niepoprawne dane logowania!");
        }

        if (activeAccountTokenRepository.findByUserId(user.getId()).isPresent()) {
            return TokenResponse.badRequestToken(3, "Konto nie zostało aktywowane. Wyszukaj email i aktywuj konto!");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                identifier,
                request.password()
        ));

        var jwtToken = jwtService.generateToken(user);

        return TokenResponse.ok("Zalogowano prawidłowo", jwtToken);
    }
}
