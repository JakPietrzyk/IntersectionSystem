package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StarvationCounterManager {
    private final Map<DirectionTurnPair, Integer> starvationCounters = new HashMap<>();
    private final VehicleCounter vehicleCounter;

    public StarvationCounterManager(Map<CompassDirection, Road> roads, VehicleCounter vehicleCounter) {
        initializeStarvationCounters(roads);
        this.vehicleCounter = vehicleCounter;
    }

    private void initializeStarvationCounters(Map<CompassDirection, Road> roads) {
        for (Map.Entry<CompassDirection, Road> entry : roads.entrySet()) {
            CompassDirection direction = entry.getKey();
            Road road = entry.getValue();

            road.getRoadLineLights().forEach((roadLine, light) ->
                    starvationCounters.put(new DirectionTurnPair(direction, roadLine.getAllowedDirections()), 0)
            );
        }
    }


    public void updateStarvationCounters(Set<DirectionTurnPair> currentDirection) {
        for (var entry : starvationCounters.entrySet()) {
            updateStarvationForDirectionsIfNeeded(entry.getKey(), entry.getValue(), currentDirection);
        }
    }

    private void updateStarvationForDirectionsIfNeeded(DirectionTurnPair directionTurnPair,
                                                       int currentCounter,
                                                       Set<DirectionTurnPair> currentDirections) {
        if (isRoadLineForDirectionEmpty(directionTurnPair)
                && isDirectionOrTurnInactive(directionTurnPair, currentDirections)) {
            starvationCounters.put(directionTurnPair, currentCounter + 1);
        }
    }

    private boolean isRoadLineForDirectionEmpty(DirectionTurnPair directionTurnPair) {
        return vehicleCounter.calculateVehiclesOnEachRoadLine()
                .getOrDefault(directionTurnPair, 0) > 0;
    }

    private boolean isDirectionOrTurnInactive(DirectionTurnPair directionTurnPair,
                                              Set<DirectionTurnPair> currentDirections) {
        return !currentDirections.contains(directionTurnPair);
    }

    public Map<DirectionTurnPair, Integer> getStarvationCounters() {
        return Map.copyOf(starvationCounters);
    }

    public void clearStarvationForDirection(DirectionTurnPair directionTurnPair) {
        starvationCounters.put(directionTurnPair, 0);
    }
}
