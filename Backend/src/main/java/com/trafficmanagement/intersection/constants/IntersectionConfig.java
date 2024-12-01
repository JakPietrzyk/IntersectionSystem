package com.trafficmanagement.intersection.constants;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.intersections.IntensityHandlingIntersection;
import com.trafficmanagement.intersection.components.intersections.Intersection;
import com.trafficmanagement.intersection.components.intersections.SequentionalIntersection;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@Configuration
public class IntersectionConfig {
    @Value("${intersection.type}")
    private String intersectionType;

    @Bean
    @Scope("prototype")
    public Intersection intersection() {
        Map<CompassDirection, Road> roads = Map.of(
                CompassDirection.NORTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.EAST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.SOUTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.WEST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                ))
        );

        if ("intensity".equalsIgnoreCase(intersectionType)) {
            return new IntensityHandlingIntersection(roads);
        } else {
            return new SequentionalIntersection(roads);
        }
    }
}