# Commute range problem

The CEO of the small company that you work for has asked you to build a new feature which allows users of the app to determine the reachable towns/cities from a starting point in a given amount of time. For example, a user may pick the city of San Francisco, and a commute time of 60 minutes and the new feature should list the cities/towns which are reachable from San Francisco within 60 minutes [ input : (city, time) ­> output listOf (city, city, city, city) ]

For the purposes of this exercise real­time data does not need to be considered, rather a static immutable dataset can be assumed to be available and correct. Also assume that you can receive the data in the format that you need, e.g. commute time between various cities.

## Solution

 - The application is a restful service built with Spring Boot and JAX-RS. JAX-RS was chosen because of its clear and simple method of defining REST endpoints.
 - There is a static immutable dataset stored in MongoDB. MongoDB was chosen because it allows you to quickly read the data.
 - The cities search algorithm works on the principle of constructing roads between cities, taking into account the time spent on the road. If the constructed road does not exceed the specified time, then the city added to the road can be added to the result.
 - For each city responses with maximum _time_ parameter are saved. Cache items contain city, time and map of reachable cities with its time to reach sorted by time.
 
## Configuration

```
server.port=8080

#mongodb configuration
mongodb.name=crp
mongodb.host=localhost
mongodb.port=27018
mongodb.connections=1000

#redis configuration
spring.redis.host=localhost
spring.redis.port=6380
```

## Usage

To start the app:
1. In command line open the root folder
2. Run `docker-compose up` 
3. Run `gradlew bootRun`

Uncomment `mongo-seed` block in `docker-compose.yaml` before starting if you want to initialize data. You can add your data in `${project_root}/mongo-seed/init.json`.

## Example

###Dataset example:
```json
[
    { "cities" : [ "London", "Birmingham" ], "time" : 900 },
    { "cities" : [ "Manchester", "Birmingham" ], "time" : 1000 },
    { "cities" : [ "Manchester", "Leeds" ], "time" : 300 },
    { "cities" : [ "London", "Edinburgh" ], "time" : 2400 }
]
```

###Input example:

```GET localhost:8080/api?city=London&time=2000```


###Output example:
```json
[
    "Manchester",
    "Birmingham"
]
```
