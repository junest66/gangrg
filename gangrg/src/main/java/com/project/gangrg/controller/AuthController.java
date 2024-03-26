package com.project.gangrg.controller;

import static com.project.gangrg.common.constant.Constants.*;

import com.project.gangrg.common.ApiResponse;
import com.project.gangrg.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authService.getRefreshTokenFromCookie(request.getCookies());
        Map<String, String> newTokens = authService.reissue(refreshToken);
        response.setHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + newTokens.get(ACCESS));
        response.addCookie(createCookie(REFRESH, newTokens.get(REFRESH), COOKIE_REFRESH_EXPIRATION_SECONDS));
        return ApiResponse.successWithNoContent();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authService.getRefreshTokenFromCookie(request.getCookies());
        authService.logout(refreshToken);
        response.addCookie(createCookie(REFRESH, null, 0));
        return ApiResponse.successWithNoContent();
    }

    private Cookie createCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
