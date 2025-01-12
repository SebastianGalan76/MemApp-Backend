package com.coresaken.memApp.auth.dto.response;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.data.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SignInResponse extends Response {
    private final String token;
    private final UserDto user;

    public SignInResponse(String message, int errorCode){
        super(false, message, errorCode);
        token = null;
        user = null;
    }
    public SignInResponse(String message, String token, UserDto userDto){
        super(true, message, -1);
        this.token = token;
        this.user = userDto;
    }

    public static ResponseEntity<SignInResponse> ok(String message, String token, UserDto userDto){
        return ResponseEntity.ok(new SignInResponse(message, token, userDto));
    }

    public static ResponseEntity<SignInResponse> badRequestToken(int code, String message){
        return ResponseEntity.badRequest().body(new SignInResponse(message, code));
    }
}
