package com.example.commuterange.repository;

import com.example.commuterange.domain.Road;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface RoadRepository extends Repository<Road, String> {

    List<Road> findAllByCitiesContains(String city);
}
