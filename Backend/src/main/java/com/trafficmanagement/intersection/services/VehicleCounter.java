package com.trafficmanagement.intersection.services;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.HashMap;
import java.util.Map;

public class VehicleCounter {
    private final Map<CompassDirection, Road> roads;

    public VehicleCounter(Map<CompassDirection, Road> roads) {
        this.roads = roads;
    }

    public Map<DirectionTurnPair, Integer> calculateVehiclesOnEachRoadLine() {
        Map<DirectionTurnPair, Integer> result = new HashMap<>();
        roads.forEach((compassDirection, road) -> road.getRoadLineLights().forEach((roadLine, light) -> {
            var count = roadLine.getVehicleCount();
            var key = new DirectionTurnPair(compassDirection, roadLine.getAllowedDirections());
            var currentValue = result.getOrDefault(key, 0);
            result.put(key, currentValue + count);
        }));

        return result;
    }
}
