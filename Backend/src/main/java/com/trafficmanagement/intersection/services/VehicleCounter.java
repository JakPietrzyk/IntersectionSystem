package com.trafficmanagement.intersection.services;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.roadlines.RoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VehicleCounter {
    private final Map<CompassDirection, Road> roads;

    public VehicleCounter(Map<CompassDirection, Road> roads) {
        this.roads = roads;
    }

    public Map<CompassDirection, Map<TurnDirection, Integer>> calculateVehiclesOnEachRoadLine() {
        return roads.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<TurnDirection, Integer> turnCounts = new EnumMap<>(TurnDirection.class);
                            addAllVehiclesOnEachRoadLineForRoad(entry, turnCounts);
                            return turnCounts;
                        }
                ));
    }

    private static void addAllVehiclesOnEachRoadLineForRoad(Map.Entry<CompassDirection, Road> entry,
                                                            Map<TurnDirection, Integer> turnCounts) {
        for (RoadLine line : getRoadLines(entry)) {
            for (TurnDirection direction : line.getAllowedDirections()) {
                addCountedVehicles(line, direction, turnCounts);
            }
        }
    }

    private static Set<RoadLine> getRoadLines(Map.Entry<CompassDirection, Road> entry) {
        return entry.getValue().getRoadLineLights().keySet();
    }

    private static void addCountedVehicles(RoadLine line, TurnDirection direction,
                                           Map<TurnDirection, Integer> turnCounts) {
        turnCounts.merge(direction, line.getVehicleCountForTurnDirection(direction), Integer::sum);
    }
}
