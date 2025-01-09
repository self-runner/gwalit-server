package com.selfrunner.inframodule.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisClient {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis에 key-value 저장
     * @param key 저장할 key
     * @param value 저장할 value
     * @param timeout 데이터 유효시간 (expire time)
     */
    public RedisDto setValue(String key, String value, Long timeout) {
        // Redis 서버가 정상적으로 동작하지 않을 경우
        if(!isRedisAvailable()) {
            return RedisDto.builder()
                    .key(key)
                    .value(value)
                    .isSuccess(false)
                    .build();
        }

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, Duration.ofMinutes(timeout));

        return RedisDto.builder()
                .key(key)
                .value(value)
                .isSuccess(true)
                .build();
    }

    /**
     * Redis에서 key로 value 조회
     * @param key 조회할 key
     * @return key에 해당하는 value
     */
    public RedisDto getValue(String key) {
        if(!isRedisAvailable()) {
            return RedisDto.builder()
                    .key(key)
                    .isSuccess(false)
                    .build();
        }

        ValueOperations<String, String> values = redisTemplate.opsForValue();

        return RedisDto.builder()
                .key(key)
                .value(values.get(key))
                .isSuccess(true)
                .build();
    }

    /**
     * Redis에서 key로 value 삭제
     * @param key 삭제할 key
     */
    public void deleteValue(String key) {
        // Redis 정상 동작 시에만 삭제
        if(isRedisAvailable()) {
            redisTemplate.delete(key);
        }
    }

    /**
     * Redis 서버가 정상적으로 동작하는지 확인
     * @return Redis 서버 동작 여부
     */
    private boolean isRedisAvailable() {
        try {
            return Optional.ofNullable(redisTemplate.getConnectionFactory())
                    .map(connectionFactory -> (connectionFactory.getConnection().ping() != null))
                    .orElse(Boolean.TRUE);
        } catch (Exception e) {
            return false;
        }
    }
}