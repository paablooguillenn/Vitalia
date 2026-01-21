package com.citas.citas_medicas_backend.dto;

public class RefreshRequest {

    private String refreshToken;

    public RefreshRequest() {}

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}