package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;

import java.util.*;

public class StarvationCounterManager {
    private final Map<CompassDirection, Map<TurnDirection, Integer>> starvationCounters = new HashMap<>();
    private final VehicleCounter vehicleCounter;

    public StarvationCounterManager(Map<CompassDirection, Road> roads, VehicleCounter vehicleCounter) {
        initializeStarvationCounters(roads);
        this.vehicleCounter = vehicleCounter;
    }

    private void initializeStarvationCounters(Map<CompassDirection, Road> roads) {
        for (CompassDirection direction : roads.keySet()) {
            Map<TurnDirection, Integer> turnCounters = new EnumMap<>(TurnDirection.class);
            for (TurnDirection turn : TurnDirection.values()) {
                turnCounters.put(turn, 0);
            }
            starvationCounters.put(direction, turnCounters);
        }
    }

    public void updateStarvationCounters(Set<CompassDirection> currentCompassDirection, Set<TurnDirection> currentTurnDirection) {
        for (var entry : starvationCounters.entrySet()) {
            CompassDirection compassDirection = entry.getKey();
            Map<TurnDirection, Integer> turnCounters = entry.getValue();

            for (TurnDirection turnDirection : turnCounters.keySet()) {
                updateStarvationForDirectionsIfNeeded(turnDirection, compassDirection, turnCounters, currentCompassDirection, currentTurnDirection);
            }
        }
    }

    private void updateStarvationForDirectionsIfNeeded(TurnDirection turnDirection, CompassDirection compassDirection, Map<TurnDirection, Integer> turnCounters, Set<CompassDirection> currentCompassDirection, Set<TurnDirection> currentTurnDirection) {
        if (isRoadLineForDirectionEmpty(turnDirection, compassDirection)
                && isDirectionOrTurnInactive(turnDirection, compassDirection, currentCompassDirection, currentTurnDirection)) {
            turnCounters.put(turnDirection, turnCounters.get(turnDirection) + 1);
        }
    }

    private boolean isRoadLineForDirectionEmpty(TurnDirection turnDirection, CompassDirection compassDirection) {
        return vehicleCounter.calculateVehiclesOnEachRoadLine().getOrDefault(compassDirection, Map.of()).getOrDefault(turnDirection, 0) > 0;
    }

    private boolean isDirectionOrTurnInactive(TurnDirection turnDirection, CompassDirection compassDirection, Set<CompassDirection> currentCompassDirections, Set<TurnDirection> currentTurnDirections) {
        return !currentCompassDirections.contains(compassDirection) || !currentTurnDirections.contains(turnDirection);
    }

    public Map<CompassDirection, Map<TurnDirection, Integer>> getStarvationCounters() {
        return Map.copyOf(starvationCounters);
    }

    public void clearStarvationForDirection(DirectionTurnPair directionTurnPair) {
        starvationCounters.get(directionTurnPair.compassDirection()).put(directionTurnPair.turnDirection(), 0);
    }
}
