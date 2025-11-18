package com.example.rating_system.Services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 24, TimeUnit.HOURS);
    }

    public String getCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void setValue(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void deleteCode(String email) {
        redisTemplate.delete(email);
    }
}
