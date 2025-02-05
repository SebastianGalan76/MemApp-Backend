package com.coresaken.memApp.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class ObjectResponse<T> {
    final boolean success;
    final String message;
    final int errorCode;

    final T object;

    public static <T> ResponseEntity<ObjectResponse<T>> ok(String message, T object){
        return ResponseEntity.ok(new ObjectResponse<>(true, message, -1, object));
    }

    public static <T> ResponseEntity<ObjectResponse<T>> badRequest(int errorCode, String message){
        return ResponseEntity.badRequest().body(new ObjectResponse<T>(false, message, errorCode, null));
    }
}