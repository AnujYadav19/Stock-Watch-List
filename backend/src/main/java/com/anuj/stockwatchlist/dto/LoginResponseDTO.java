package com.anuj.stockwatchlist.dto;

public class LoginResponseDTO {

    private String accessToken;

    private String tokenType;

    private long expiresIn;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String accessToken, String tokenType, long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO [tokenType=" + tokenType + ", expiresIn=" + expiresIn + "]";
    }

}
