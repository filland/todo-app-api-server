package com.kurbatov.todoapp.security.oauth2;

public enum AuthProvider {
    LOCAL,
    FACEBOOK,
    GOOGLE,
    GITHUB;

    public static AuthProvider toEnum(String authProvider) {
        for (AuthProvider provider : values()) {
            if (provider.toString().equalsIgnoreCase(authProvider)) {
                return provider;
            }
        }
        return null;
    }
}
