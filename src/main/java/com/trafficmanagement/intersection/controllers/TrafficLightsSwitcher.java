package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.Map;
import java.util.Set;

public class TrafficLightsSwitcher {
    private final Map<CompassDirection, Road> roads;

    public TrafficLightsSwitcher(Map<CompassDirection, Road> roads) {
        this.roads = roads;
        setUpLights();
    }

    private void setUpLights() {
        roads.get(CompassDirection.NORTH).setGreenTrafficLight(TurnDirection.STRAIGHT);
        roads.get(CompassDirection.SOUTH).setGreenTrafficLight(TurnDirection.STRAIGHT);
    }

    public LightColor getCurrentTrafficLight(CompassDirection compassDirection, TurnDirection turnDirection) {
        return roads.get(compassDirection).getCurrentLightColor(turnDirection);
    }

    public void switchLightsToRedForCompassDirections(Set<CompassDirection> compassDirections) {
        compassDirections.forEach(compassDirection -> roads.get(compassDirection).setRedTrafficLightForAllRoadLines());
    }

    public void switchLightsToGreenForDirections(CompassDirection compassDirection, TurnDirection turnDirection) {
        roads.get(compassDirection).setGreenTrafficLight(turnDirection);
    }

    public void switchOppositeLights(TurnDirection turnDirection) {
        roads.values().forEach(road -> road.changeLightColor(turnDirection));
    }

    public void switchLightsForDirections(Set<CompassDirection> compassDirections, Set<TurnDirection> turnDirections) {
        compassDirections.forEach(direction ->
                turnDirections.forEach(turn ->
                        roads.get(direction).changeLightColor(turn)
                )
        );
    }

}
