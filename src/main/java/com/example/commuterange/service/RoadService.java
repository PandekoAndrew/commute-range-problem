package com.example.commuterange.service;

import com.example.commuterange.domain.Road;

import java.util.List;

public interface RoadService {

    List<Road> getNeighbors(String city);
}
