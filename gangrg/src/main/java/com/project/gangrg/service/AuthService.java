package com.project.gangrg.service;

import static com.project.gangrg.common.constant.Constants.ACCESS;
import static com.project.gangrg.common.constant.Constants.REFRESH;
import static com.project.gangrg.common.exception.ErrorCode.INVALID_REQUEST;
import static com.project.gangrg.common.exception.ErrorCode.REFRESH_TOKEN_EXPIRED;

import com.project.gangrg.common.exception.CustomException;
import com.project.gangrg.common.jwt.JWTUtil;
import com.project.gangrg.dto.RefreshTokenDto;
import com.project.gangrg.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public String getRefreshTokenFromCookie(Cookie[] cookies) {
        String refresh = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH)) {
                refresh = cookie.getValue();
                break;
            }
        }
        if (refresh == null) {
            throw new CustomException(INVALID_REQUEST);
        }
        return refresh;
    }

    public Map<String, String> reissue(String refreshToken) {
        validateRefreshToken(refreshToken);
        String userId = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> new CustomException(INVALID_REQUEST)).getUserId();
        String username = jwtUtil.getUsername(refreshToken);
        String nickname = jwtUtil.getNickname(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createAccessToken(Long.valueOf(userId), username, nickname, role);
        String newRefreshToken = jwtUtil.createRefreshToken(Long.valueOf(userId), username, nickname, role);

        refreshTokenRepository.deleteById(refreshToken);
        refreshTokenRepository.save(new RefreshTokenDto(newRefreshToken, userId));

        return Map.of(ACCESS, newAccessToken, REFRESH, newRefreshToken);
    }

    public void logout(String refreshToken) {
        validateRefreshToken(refreshToken);
        refreshTokenRepository.findById(refreshToken).orElseThrow(() -> new CustomException(INVALID_REQUEST));
        refreshTokenRepository.deleteById(refreshToken);
        log.info("logout success");
    }

    private void validateRefreshToken(String refreshToken) {
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new CustomException(REFRESH_TOKEN_EXPIRED);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals(REFRESH)) {
            throw new CustomException(INVALID_REQUEST);
        }
    }
}
