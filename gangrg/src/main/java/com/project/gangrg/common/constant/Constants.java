package com.project.gangrg.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    //JWT 토큰
    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 600000L; // 10분
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 86400000L; // 24시간
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final int COOKIE_REFRESH_EXPIRATION_SECONDS = 24 * 60 * 60; // 24시간
}
