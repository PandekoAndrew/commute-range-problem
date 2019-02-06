package com.example.commuterange.controller;

import com.example.commuterange.service.CommuteRangeService;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Controller
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class CommuteRangeController {

    private final CommuteRangeService commuteRangeService;

    public CommuteRangeController(CommuteRangeService commuteRangeService) {
        this.commuteRangeService = commuteRangeService;
    }

    @GET
    public Response getReachableCities(@QueryParam("city") String city, @QueryParam("time") Integer time) {
        return Response.ok(commuteRangeService.getReachableCities(city, time)).build();
    }
}
