package com.coresaken.memApp.auth.service;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.auth.ActiveAccountToken;
import com.coresaken.memApp.database.repository.UserRepository;
import com.coresaken.memApp.database.repository.auth.ActiveAccountTokenRepository;
import com.coresaken.memApp.service.async.AsyncAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActiveAccountService {
    private final ActiveAccountTokenRepository activeAccountTokenRepository;
    private final AsyncAccountService asyncAccountService;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<Response> activeAccount(String code){
        activeAccountTokenRepository.deleteByToken(code);
        return Response.ok("Konto zostało prawidłowo aktywowane");
    }

    public ResponseEntity<Response> sendVerificationEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return Response.badRequest(1, "Brak konta o podanym adresie e-mail.");
        }

        Optional<ActiveAccountToken> activeAccountTokenOptional = activeAccountTokenRepository.findByUserId(userOptional.get().getId());
        if(activeAccountTokenOptional.isEmpty()){
            return Response.badRequest(2, "Konto jest już aktywowane.");
        }

        asyncAccountService.sendVerificationEmail(email, activeAccountTokenOptional.get().getToken());
        return Response.ok("Wiadomość aktywująca konto została ponownie wysłana.");
    }
}
