package com.example.commuterange.config;

import com.example.commuterange.controller.CommuteRangeController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(CommuteRangeController.class);
    }
}
