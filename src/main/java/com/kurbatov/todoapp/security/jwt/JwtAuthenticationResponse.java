package com.kurbatov.todoapp.security.jwt;

public class JwtAuthenticationResponse {

    private final static String TOKEN_TYPE = "Bearer";
    private String accessToken;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return TOKEN_TYPE;
    }
}
