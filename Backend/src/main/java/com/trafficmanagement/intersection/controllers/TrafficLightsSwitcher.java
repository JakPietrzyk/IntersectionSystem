package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.Map;
import java.util.Set;

public class TrafficLightsSwitcher {
    private final Map<CompassDirection, Road> roads;

    public TrafficLightsSwitcher(Map<CompassDirection, Road> roads) {
        this.roads = roads;
    }

    public void switchLightsToRedForCompassDirections(Set<CompassDirection> compassDirections) {
        compassDirections.forEach(compassDirection -> roads.get(compassDirection).setRedTrafficLightForAllRoadLines());
    }

    public void switchLightsToGreenForRoadLines(DirectionTurnPair directionTurnPair) {
        roads.get(directionTurnPair.compassDirection()).setGreenTrafficLight(directionTurnPair.turnDirections());
    }
}
