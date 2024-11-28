package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.TrafficLightsSwitcher;

import java.util.EnumSet;
import java.util.Map;

public abstract class LightsController {
    protected final TrafficLightsSwitcher lightsSwitcher;
    protected final Map<CompassDirection, Road> roads;
    protected EnumSet<TurnDirection> currentTurnDirection;
    protected EnumSet<CompassDirection> currentCompassDirections;
    protected int stepCounter = 0;

    protected LightsController(Map<CompassDirection, Road> roads) {
        this.roads = roads;
        this.lightsSwitcher = new TrafficLightsSwitcher(roads);
        currentCompassDirections = EnumSet.of(CompassDirection.NORTH, CompassDirection.SOUTH);
        currentTurnDirection = EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT);
    }

    public abstract void makeStep();
}
