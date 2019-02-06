package com.example.commuterange.service;

import java.util.Set;

public interface CommuteRangeService {

    Set<String> getReachableCities(String city, Integer time);
}
