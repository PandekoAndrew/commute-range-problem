package com.example.commuterange.service.impl;

import com.example.commuterange.domain.Road;
import com.example.commuterange.repository.RoadRepository;
import com.example.commuterange.service.RoadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoadServiceImpl implements RoadService {

    private final RoadRepository roadRepository;

    public RoadServiceImpl(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
    }

    @Override
    public List<Road> getNeighbors(String city) {
        return roadRepository.findAllByCitiesContains(city);
    }
}
