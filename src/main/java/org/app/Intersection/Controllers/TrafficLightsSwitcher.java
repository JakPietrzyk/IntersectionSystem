package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Components.Road;
import org.app.Intersection.Constants.TurnDirection;

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

    public void switchLights(TurnDirection turnDirection) {
        roads.values().forEach(road -> road.changeLightColor(turnDirection));
    }
}
