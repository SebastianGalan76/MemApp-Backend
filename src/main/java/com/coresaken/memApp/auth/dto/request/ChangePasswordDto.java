package com.coresaken.memApp.auth.dto.request;

public record ChangePasswordDto(String token, String newPassword) {
}
