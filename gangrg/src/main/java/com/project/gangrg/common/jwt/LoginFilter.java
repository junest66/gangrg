package com.project.gangrg.common.jwt;

import static com.project.gangrg.common.constant.Constants.AUTHORIZATION_HEADER;
import static com.project.gangrg.common.constant.Constants.BEARER_TOKEN_PREFIX;
import static com.project.gangrg.common.constant.Constants.COOKIE_REFRESH_EXPIRATION_SECONDS;
import static com.project.gangrg.common.constant.Constants.REFRESH;
import static com.project.gangrg.common.exception.ErrorCode.LOGIN_FAILURE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gangrg.common.ApiResponse;
import com.project.gangrg.common.exception.CustomException;
import com.project.gangrg.domain.User;
import com.project.gangrg.dto.CustomUserDetails;
import com.project.gangrg.dto.LoginDto;
import com.project.gangrg.dto.RefreshTokenDto;
import com.project.gangrg.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto;
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드(jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        String role = getRole(authentication);
        String access = jwtUtil.createAccessToken(user.getId(), username, user.getNickname(), role);
        String refresh = jwtUtil.createRefreshToken(user.getId(), username, user.getNickname(), role);

        refreshTokenRepository.save(new RefreshTokenDto(refresh, String.valueOf(user.getId())));
        response.setHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + access);
        response.addCookie(createCookie(REFRESH, refresh));
        response.setStatus(HttpStatus.OK.value());
        sendSuccessResponse(response);
        log.info("login success");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("login fail");
        throw new CustomException(LOGIN_FAILURE);
    }

    private String getRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        return auth.getAuthority();
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_REFRESH_EXPIRATION_SECONDS);
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void sendSuccessResponse(HttpServletResponse response) throws IOException {
        ApiResponse<?> apiResponse = ApiResponse.successWithNoContent();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writeValueAsString(apiResponse));
        writer.flush();
    }
}
