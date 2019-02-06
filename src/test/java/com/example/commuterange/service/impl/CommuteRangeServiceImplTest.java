package com.example.commuterange.service.impl;

import com.example.commuterange.domain.CacheItem;
import com.example.commuterange.domain.Road;
import com.example.commuterange.service.CacheService;
import com.example.commuterange.service.RoadService;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CommuteRangeServiceImplTest {

    @InjectMocks
    private CommuteRangeServiceImpl commuteRangeService;
    @Mock
    private RoadService roadService;
    @Mock
    private CacheService cacheService;

    @BeforeTest
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void reset() {
        Mockito.reset(roadService, cacheService);
    }

    @Test(dataProvider = "input")
    public void getReachableCitiesNullCacheTestOk(String inputCity, Integer inputTime, Set<String> expectedResult) {
        when(cacheService.getCacheByCity(inputCity)).thenReturn(null);

        Map<String, List<Road>> map = roads();

        for (String city : map.keySet()) {
            when(roadService.getNeighbors(city)).thenReturn(map.get(city));
        }

        Set<String> reachableCities = commuteRangeService.getReachableCities(inputCity, inputTime);

        verify(cacheService).save(any());

        for (String expectedCity : expectedResult) {
            assertTrue(reachableCities.contains(expectedCity));
        }
        assertEquals(reachableCities.size(), expectedResult.size());
    }

    @Test(dataProvider = "inputWithCache")
    public void getReachableCitiesWithCacheTestOk(String inputCity, Integer inputTime, Set<String> expectedResult, CacheItem cacheItem) {

        when(cacheService.getCacheByCity(inputCity)).thenReturn(cacheItem);

        Set<String> reachableCities = commuteRangeService.getReachableCities(inputCity, inputTime);

        verify(roadService, never()).getNeighbors(any());

        for (String expectedCity : expectedResult) {
            assertTrue(reachableCities.contains(expectedCity));
        }
        assertEquals(reachableCities.size(), expectedResult.size());
    }

    private Map<String, List<Road>> roads() {
        Map<String, List<Road>> map = new HashMap<>();

        map.put("London", asList(
                new Road(asList("London", "Birmingham"), 900),
                new Road(asList("Edinburgh", "London"), 2400)
        ));

        map.put("Birmingham", asList(
                new Road(asList("London", "Birmingham"), 900),
                new Road(asList("Birmingham", "Manchester"), 1000),
                new Road(asList("Liverpool", "Birmingham"), 600)
        ));

        map.put("Edinburgh", asList(
                new Road(asList("Edinburgh", "Liverpool"), 700),
                new Road(asList("Edinburgh", "London"), 2400)
        ));

        map.put("Liverpool", asList(
                new Road(asList("Liverpool", "Manchester"), 200),
                new Road(asList("Edinburgh", "Liverpool"), 700),
                new Road(asList("Liverpool", "Birmingham"), 600)
        ));

        map.put("Manchester", asList(
                new Road(asList("Manchester", "Leeds"), 300),
                new Road(asList("Liverpool", "Manchester"), 200),
                new Road(asList("Birmingham", "Manchester"), 1000)
        ));

        map.put("Leeds", asList(
                new Road(asList("Manchester", "Leeds"), 300)
        ));

        map.put("Dublin", asList(
                new Road(asList("Dublin", "Galway"), 400),
                new Road(asList("Limerick", "Dublin"), 500)
        ));

        map.put("Galway", asList(
                new Road(asList("Dublin", "Galway"), 400),
                new Road(asList("Galway", "Limerick"), 300),
                new Road(asList("Galway", "Westport"), 650)
        ));

        map.put("Limerick", asList(
                new Road(asList("Galway", "Limerick"), 300),
                new Road(asList("Limerick", "Dublin"), 500)
        ));

        map.put("Westport", asList(
                new Road(asList("Galway", "Westport"), 650)
        ));

        return map;
    }

    @DataProvider(name = "input")
    public Object[][] inputData() {

        return new Object[][]{
                new Object[]{"London", 2000, new HashSet<>(asList("Birmingham", "Manchester", "Liverpool", "Leeds"))},
                new Object[]{"Dublin", 5000, new HashSet<>(asList("Westport", "Limerick", "Galway"))},
                new Object[]{"London", 100, new HashSet<>()},
                new Object[]{"Liverpool", 1500, new HashSet<>(asList("Leeds", "Manchester", "London", "Edinburgh", "Birmingham"))}

        };
    }

    @DataProvider(name = "inputWithCache")
    public Object[][] inputDataWithCache() {
        List<CacheItem> cacheItems = cacheItems();

        return new Object[][]{
                new Object[]{"London", 2000, new HashSet<>(asList("Birmingham", "Manchester", "Liverpool", "Leeds")), cacheItems.get(0)},
                new Object[]{"Dublin", 5000, new HashSet<>(asList("Westport", "Limerick", "Galway")), cacheItems.get(1)},
                new Object[]{"London", 100, new HashSet<>(), cacheItems.get(0)},
                new Object[]{"Liverpool", 1500, new HashSet<>(asList("Leeds", "Manchester", "London", "Edinburgh", "Birmingham")), cacheItems.get(2)}
        };
    }

    private List<CacheItem> cacheItems() {
        CacheItem cacheItemLondon2000 = new CacheItem();
        cacheItemLondon2000.setCity("London");
        cacheItemLondon2000.setTime(2000);
        cacheItemLondon2000.setResult(
                ImmutableMap.<String, Integer>builder()
                        .put("Birmingham", 900)
                        .put("Liverpool", 1500)
                        .put("Manchester", 1700)
                        .put("Leeds", 2000)
                        .build()
        );

        CacheItem cacheItemDublin5000 = new CacheItem();
        cacheItemDublin5000.setCity("Dublin");
        cacheItemDublin5000.setTime(5000);
        cacheItemDublin5000.setResult(
                ImmutableMap.<String, Integer>builder()
                        .put("Galway", 300)
                        .put("Limerick", 500)
                        .put("Westport", 950)
                        .build()
        );

        CacheItem cacheItemLiverpool1500 = new CacheItem();
        cacheItemLiverpool1500.setCity("Liverpool");
        cacheItemLiverpool1500.setTime(1500);
        cacheItemLiverpool1500.setResult(
                ImmutableMap.<String, Integer>builder()
                        .put("Manchester", 200)
                        .put("Leeds", 500)
                        .put("Birmingham", 600)
                        .put("Edinburgh", 700)
                        .put("London", 1500)
                        .build()
        );

        return asList(cacheItemLondon2000, cacheItemDublin5000, cacheItemLiverpool1500);
    }
}