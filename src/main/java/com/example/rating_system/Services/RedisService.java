package com.example.rating_system.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 24, TimeUnit.HOURS);
    }

    public String getCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void deleteCode(String email) {
        redisTemplate.delete(email);
    }
}
