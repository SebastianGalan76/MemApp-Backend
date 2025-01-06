package com.coresaken.memApp.auth.dto.response;

import com.coresaken.memApp.data.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TokenResponse extends Response {
    private final String token;

    public TokenResponse(String message, int errorCode){
        super(false, message, errorCode);
        token = null;
    }
    public TokenResponse(String message, String token){
        super(true, message, -1);
        this.token = token;
    }

    public static ResponseEntity<TokenResponse> ok(String message, String token){
        return ResponseEntity.ok(new TokenResponse(message, token));
    }

    public static ResponseEntity<TokenResponse> badRequestToken(int code, String message){
        return ResponseEntity.badRequest().body(new TokenResponse(message, code));
    }
}
