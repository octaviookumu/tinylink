package com.octaviookumu.tinylink.services.impl;

import com.octaviookumu.tinylink.domain.models.RateLimitData;
import com.octaviookumu.tinylink.services.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "ratelimit:";

    @Value("${tinylink.rate-limit.requests-per-minute}")
    private int requestsPerMinute;

    @Value("${tinylink.rate-limit.requests-per-hour}")
    private int requestsPerHour;

    // our db is the rateLimitData
    private final ConcurrentHashMap<String, RateLimitData> rateLimitData =
            new ConcurrentHashMap<>(); // ConcurrentHashMap is thread safe


    @Override
    public boolean isAllowed(String clientIp) {
        String redisKey = REDIS_KEY_PREFIX + clientIp;

        LocalDateTime now = LocalDateTime.now();

        RateLimitData data = getRateLimitDataFromRedis(redisKey);

        if (data == null) {
            data = rateLimitData.computeIfAbsent(clientIp, k -> RateLimitData.builder()
                    .minuteCount(0)
                    .hourCount(0)
                    .minuteWindowStart(now)
                    .build()); // when the data is not present inside redis
        }




        return false;
    }

    private RateLimitData getRateLimitDataFromRedis(String redisKey) {
        // catch if redis is not available
        try {
            return (RateLimitData) redisTemplate.opsForValue().get(redisKey);
        } catch (Exception e) {
            log.warn("Failed to get rate limit data from Redis: {}", e.getMessage());
            return null; // then data will come from database
        }

    }
}
