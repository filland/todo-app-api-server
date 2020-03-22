package com.kurbatov.todoapp.security.oauth2;

/**
 * Describes how the user registered in the app
 */
public enum AuthProvider {
    // means that user registered using email/password
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
