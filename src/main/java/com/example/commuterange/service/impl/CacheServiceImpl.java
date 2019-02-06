package com.example.commuterange.service.impl;

import com.example.commuterange.domain.CacheItem;
import com.example.commuterange.repository.CacheRepository;
import com.example.commuterange.service.CacheService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

@Service
public class CacheServiceImpl implements CacheService {

    private final CacheRepository cacheRepository;

    public CacheServiceImpl(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Override
    public CacheItem getCacheByCity(String city) {
        return cacheRepository.findCacheByCity(city);
    }

    @Override
    @Async
    public void save(CacheItem cacheItem) {
        CacheItem oldCacheItem = getCacheByCity(cacheItem.getCity());

        if (oldCacheItem == null || oldCacheItem.getTime() < cacheItem.getTime()) {
            LinkedHashMap<String, Integer> sorted = cacheItem.getResult().entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            cacheItem.setResult(sorted);
            cacheRepository.save(cacheItem);
        }
    }
}
