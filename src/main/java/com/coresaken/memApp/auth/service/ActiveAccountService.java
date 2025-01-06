package com.coresaken.memApp.auth.service;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.repository.auth.ActiveAccountTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActiveAccountService {
    private final ActiveAccountTokenRepository activeAccountTokenRepository;

    public ResponseEntity<Response> activeAccount(String code){
        activeAccountTokenRepository.deleteByToken(code);
        return Response.ok("Konto zostało prawidłowo aktywowane");
    }
}
