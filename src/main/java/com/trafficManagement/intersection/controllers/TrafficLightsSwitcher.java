package com.trafficManagement.intersection.controllers;

import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.LightColor;
import com.trafficManagement.intersection.components.Road;
import com.trafficManagement.intersection.constants.TurnDirection;

import java.util.EnumSet;
import java.util.Map;

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

    public void switchLightsToRedForCompassDirections(EnumSet<CompassDirection> compassDirections) {
        compassDirections.forEach(compassDirection -> roads.get(compassDirection).setRedTrafficLightForAllRoadLines());
    }

    public void switchLightsToGreenForDirections(CompassDirection compassDirection, TurnDirection turnDirection) {
        roads.get(compassDirection).setGreenTrafficLight(turnDirection);
    }

    public void switchOppositeLights(TurnDirection turnDirection) {
        roads.values().forEach(road -> road.changeLightColor(turnDirection));
    }

    public void switchLightsOnCompassDirectionForTurnDirection(EnumSet<CompassDirection> compassDirections, EnumSet<TurnDirection> turnDirections) {
        compassDirections.forEach(direction ->
                turnDirections.forEach(turn ->
                        roads.get(direction).changeLightColor(turn)
                )
        );
    }

}
