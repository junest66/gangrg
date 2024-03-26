package com.project.gangrg.common.jwt;

import static com.project.gangrg.common.constant.Constants.ACCESS;
import static com.project.gangrg.common.constant.Constants.AUTHORIZATION_HEADER;
import static com.project.gangrg.common.constant.Constants.BEARER_TOKEN_PREFIX;
import static com.project.gangrg.common.exception.ErrorCode.ACCESS_TOKEN_EXPIRED;
import static com.project.gangrg.common.exception.ErrorCode.INVALID_REQUEST;

import com.project.gangrg.common.exception.CustomException;
import com.project.gangrg.domain.Role;
import com.project.gangrg.domain.User;
import com.project.gangrg.dto.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith(BEARER_TOKEN_PREFIX)) {
            log.info("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ACCESS_TOKEN_EXPIRED);
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals(ACCESS)) {
            throw new CustomException(INVALID_REQUEST);
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = User.builder()
                .name(username)
                .role(Role.valueOf(role))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
