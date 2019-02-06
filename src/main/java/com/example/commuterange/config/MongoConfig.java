package com.example.commuterange.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value(value = "${mongodb.name}")
    private String databaseName;
    @Value(value = "${mongodb.host}")
    private String host;
    @Value(value = "${mongodb.port}")
    private Integer port;
    @Value(value = "${mongodb.connections}")
    private Integer connectionsCount;

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(connectionsCount).build();
        return new MongoClient(new ServerAddress(host, port), mongoClientOptions);
    }
}