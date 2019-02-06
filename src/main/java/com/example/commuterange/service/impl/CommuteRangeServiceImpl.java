package com.example.commuterange.service.impl;

import com.example.commuterange.domain.CacheItem;
import com.example.commuterange.domain.Road;
import com.example.commuterange.service.CacheService;
import com.example.commuterange.service.CommuteRangeService;
import com.example.commuterange.service.RoadService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class CommuteRangeServiceImpl implements CommuteRangeService {

    private final RoadService roadService;
    private final CacheService cacheService;

    public CommuteRangeServiceImpl(RoadService roadService, CacheService cacheService) {
        this.roadService = roadService;
        this.cacheService = cacheService;
    }

    /**
     * Finds set of cities that might be reachable from entered city for entered time
     *
     * @param city from which we are looking
     * @param time for which we are looking for
     * @return set of cities
     */
    @Override
    public Set<String> getReachableCities(String city, Integer time) {
        Set<String> result = new HashSet<>();

        /*
          Looking for cache, before the algorithm starts.

          If there is a cache with more or equal time for a given city, then we take the data from the cache.
         */
        CacheItem cacheItem = cacheService.getCacheByCity(city);

        if (cacheItem != null && cacheItem.getTime() >= time) {

            for (String resultCity : cacheItem.getResult().keySet()) {

                if (cacheItem.getResult().get(resultCity) <= time) {
                    result.add(resultCity);
                } else {
                    //cities are sorted by time, so there are no more suitable cities
                    break;
                }
            }

            return result;
        }

        cacheItem = new CacheItem();
        cacheItem.setCity(city);
        cacheItem.setTime(time);
        cacheItem.setResult(new HashMap<>());

        //starting at introduced city
        Road initRoad = new Road();
        initRoad.setCities(new ArrayList<>());
        initRoad.setTime(0);
        initRoad.add(city, 0);
        List<Road> nextRoads = Collections.singletonList(initRoad);

        do {
            nextRoads = nextRoads(nextRoads, time, result, cacheItem);
        } while (!nextRoads.isEmpty()); //while there are suitable cities and enough time

        cacheService.save(cacheItem);
        return result;
    }

    /**
     * Looking for new roads adding to old roads new cities
     *
     * @param prevRoads roads from previous iteration
     * @param time      time limit for each road
     * @param result    result set where cities will be added
     * @param cacheItem item to be saved to cache
     * @return list of roads reached at the current iteration
     */
    private List<Road> nextRoads(List<Road> prevRoads, Integer time, Set<String> result, CacheItem cacheItem) {
        List<Road> nextRoads = new ArrayList<>();

        for (Road road : prevRoads) {
            List<Road> neighbors = roadService.getNeighbors(road.getLastCity());

            for (Road storageRoad : neighbors) {
                Road buffer = null;

                //two 'if' statements, because the ribs is undirected
                if (!road.contains(storageRoad.get(0))) {
                    buffer = road.copy();
                    buffer.add(storageRoad.get(0), storageRoad.getTime());
                }

                if (!road.contains(storageRoad.get(1))) {
                    buffer = road.copy();
                    buffer.add(storageRoad.get(1), storageRoad.getTime());
                }

                //if the city has not met before and there is enough time to reach it
                if (buffer != null && buffer.getTime() <= time) {
                    result.add(buffer.getLastCity());
                    nextRoads.add(buffer);
                    doCache(cacheItem, buffer.getLastCity(), buffer.getTime());
                }
            }
        }

        return nextRoads;
    }

    /**
     * Updates the cacheItem if the city has not been added before or has been added but has been reached in more time
     *
     * @param cacheItem item to be updated
     * @param city      city to be stored in cache
     * @param time      time to be stored in cache
     */
    private void doCache(CacheItem cacheItem, String city, Integer time) {
        Integer oldTime = cacheItem.getResult().get(city);

        if (oldTime == null || oldTime > time) {
            cacheItem.getResult().put(city, time);
        }
    }
}
