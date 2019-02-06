package com.example.commuterange.service;

import com.example.commuterange.domain.CacheItem;

public interface CacheService {

    CacheItem getCacheByCity(String city);

    void save(CacheItem cacheItem);
}
