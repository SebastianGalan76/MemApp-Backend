package com.coresaken.memApp.service.async;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.auth.ActiveAccountToken;
import com.coresaken.memApp.database.repository.auth.ActiveAccountTokenRepository;
import com.coresaken.memApp.service.EmailSenderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncAccountService {
    private final ActiveAccountTokenRepository activeAccountTokenRepository;
    private final EmailSenderService emailSenderService;

    @Async
    public void processAccountActivation(User user, String email, String activeAccountToken) {
        try {
            activeAccountTokenRepository.save(new ActiveAccountToken(user, activeAccountToken));
            emailSenderService.sendActiveAccountEmail(email, activeAccountToken);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendVerificationEmail(String email, String activeAccountToken) {
        try {
            emailSenderService.sendActiveAccountEmail(email, activeAccountToken);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
