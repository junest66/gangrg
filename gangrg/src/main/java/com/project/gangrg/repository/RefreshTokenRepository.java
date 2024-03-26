package com.project.gangrg.repository;

import static com.project.gangrg.common.constant.Constants.REFRESH_TOKEN_EXPIRATION_TIME;

import com.project.gangrg.dto.RefreshTokenDto;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate redisTemplate;

    public void save(final RefreshTokenDto refreshTokenDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshTokenDto.getRefreshToken(), refreshTokenDto.getUserId());
        redisTemplate.expire(refreshTokenDto.getRefreshToken(), REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshTokenDto> findById(final String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String userId = valueOperations.get(refreshToken);

        if (Objects.isNull(userId)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshTokenDto(refreshToken, userId));
    }

    public void deleteById(final String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

}
