package com.helpmeCookies.search.service;

import com.helpmeCookies.search.dto.PopularSearchResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String SEARCH_PREFIX = "search:";
    private final String RANKING_KEY = "ranking";
    private final int SEARCH_COUNT = 100;
    private final double HOUR_WEIGHT = 0.15;
    private final double DAY_WEIGHT = 0.12;
    private final double WEEK_WEIGHT = 0.04;
    private final double TOTAL_WEIGHT = 0.12;

    @Scheduled(cron = "0 0 * * * *")
    private void updatePopularSearch() {
        List<String> keys = scanKeys();
        Set<TypedTuple<Object>> values = new HashSet<>();

        var current = System.currentTimeMillis();
        long hour = current - TimeUnit.HOURS.toMillis(1);
        long day = current - TimeUnit.DAYS.toMillis(1);
        long week = current - TimeUnit.DAYS.toMillis(7);

        for (var key : keys) {
            values.add(TypedTuple.of(key.substring(SEARCH_PREFIX.length()),
                calcScore(key, hour, current, day, week)));
        }

        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.multi();

            redisTemplate.opsForZSet().removeRange(RANKING_KEY, 0, -1);
            redisTemplate.opsForZSet().add(RANKING_KEY, values);

            return connection.exec();
        });
    }

    private double calcScore(String key, long hour, long current, long day, long week) {
        var hourCount = redisTemplate.opsForZSet().count(key, hour, current);
        var dayCount = redisTemplate.opsForZSet().count(key, day, current);
        var weekCount = redisTemplate.opsForZSet().count(key, week, current);
        var totalCount = redisTemplate.opsForZSet().size(key);

        return (hourCount * HOUR_WEIGHT) + (dayCount * DAY_WEIGHT) + (weekCount * WEEK_WEIGHT) + (
            totalCount * TOTAL_WEIGHT);
    }

    private List<String> scanKeys() {
        List<String> keys = new ArrayList<>();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(redisConnection ->
            redisConnection.scan(
                ScanOptions.scanOptions().match(SEARCH_PREFIX).count(SEARCH_COUNT).build()
            )
        );

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        return keys;
    }


    public PopularSearchResponse getPopularSearch() {
        Set<Object> result = redisTemplate.opsForZSet().range(RANKING_KEY, 0, 9);
        List<String> stringResult = result.stream()
            .map(Object::toString)
            .toList();

        return new PopularSearchResponse(stringResult);
    }
}
