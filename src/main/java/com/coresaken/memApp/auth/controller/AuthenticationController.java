package com.coresaken.memApp.auth.controller;

import com.coresaken.memApp.auth.dto.request.ChangePasswordDto;
import com.coresaken.memApp.auth.dto.request.SignInRequestDto;
import com.coresaken.memApp.auth.dto.request.SignUpRequestDto;
import com.coresaken.memApp.auth.dto.response.TokenResponse;
import com.coresaken.memApp.auth.service.*;
import com.coresaken.memApp.data.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final SignInService signInService;
    private final SignUpService signUpService;
    private final LogoutService logoutService;
    private final ActiveAccountService activeAccountService;
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequestDto request){
        return signInService.signIn(request);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Response> signUp(@RequestBody SignUpRequestDto request){
        return signUpService.signUp(request);
    }

    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response){
        return logoutService.logout(request, response);
    }

    @PostMapping("/active/{code}")
    public ResponseEntity<Response> activeAccount(@PathVariable("code") String code){
        return activeAccountService.activeAccount(code);
    }

    @PostMapping("/activeAccount")
    public ResponseEntity<Response> sendVerificationEmail(@RequestParam String email) {
        return activeAccountService.sendVerificationEmail(email);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Response> resetPassword(@RequestParam String email) {
        return resetPasswordService.resetPassword(email);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return resetPasswordService.changePassword(changePasswordDto.token(), changePasswordDto.newPassword());
    }
}
