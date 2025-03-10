package com.coresaken.memApp.util;

import com.coresaken.memApp.database.model.User;

public class PermissionChecker {
    public static boolean hasPermission(User.Role role, User.Role requiredRole){
        return hasPermission(role, requiredRole.getValue());
    }

    public static boolean hasPermission(User.Role role, int requiredValue){
        return role.getValue() >= requiredValue;
    }
}
