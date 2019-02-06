package com.example.commuterange.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "roads")
public class Road {

    @Id
    private String id;
    private List<String> cities;
    private Integer time;

    public Road() {
    }

    public Road(List<String> cities, Integer time) {
        this.cities = cities;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getLastCity() {
        return cities.get(cities.size() - 1);
    }

    public String get(int index) {
        return cities.get(index);
    }

    public boolean contains(String city) {
        return cities.contains(city);
    }

    public void add(String city, Integer time) {
        cities.add(city);
        this.time += time;
    }

    public Road copy() {
        Road road = new Road();
        road.time = time;
        road.cities = new ArrayList<>();
        road.cities.addAll(cities);

        return road;
    }
}