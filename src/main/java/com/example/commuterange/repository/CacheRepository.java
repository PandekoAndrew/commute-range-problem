package com.example.commuterange.repository;

import com.example.commuterange.domain.CacheItem;
import org.springframework.data.repository.Repository;

public interface CacheRepository extends Repository<CacheItem, String> {

    CacheItem findCacheByCity(String city);

    void save(CacheItem cacheItem);
}
