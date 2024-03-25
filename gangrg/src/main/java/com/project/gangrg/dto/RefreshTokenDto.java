package com.project.gangrg.dto;

import lombok.Getter;

@Getter
public class RefreshTokenDto {

    private String refreshToken;
    private String userId;

    public RefreshTokenDto(final String refreshToken, final String userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
