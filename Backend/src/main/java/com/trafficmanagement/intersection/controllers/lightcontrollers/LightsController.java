package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.controllers.TrafficLightsSwitcher;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class LightsController {
    protected final TrafficLightsSwitcher lightsSwitcher;
    protected final Map<CompassDirection, Road> roads;
    protected Set<DirectionTurnPair> currentDirections;
    protected int stepCounter = 0;

    protected LightsController(Map<CompassDirection, Road> roads) {
        validateRoadsMap(roads);
        this.roads = roads;
        this.lightsSwitcher = new TrafficLightsSwitcher(roads);
        this.currentDirections = new HashSet<>();
        roads.forEach((compassDirection, road) -> road.getRoadLineLights().forEach((roadLine, light) -> {
            if (light.isGreenLight()) {
                currentDirections.add(new DirectionTurnPair(compassDirection, roadLine.getAllowedDirections()));
            }
        }));
    }

    private void validateRoadsMap(Map<CompassDirection, Road> roads) {
        EnumSet<CompassDirection> providedDirections = EnumSet.copyOf(roads.keySet());

        EnumSet<CompassDirection> missingDirections = EnumSet.complementOf(providedDirections);
        if (!missingDirections.isEmpty()) {
            throw new IllegalArgumentException("Roads map is missing directions: " + missingDirections);
        }
    }

    public abstract void makeStep();
}
