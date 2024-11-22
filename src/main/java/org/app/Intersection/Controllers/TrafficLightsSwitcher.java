package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Components.Road;

import java.util.Map;

public class TrafficLightsSwitcher {
    private final Map<CompassDirection, Road> roads;

    public TrafficLightsSwitcher(Map<CompassDirection, Road> roads) {
        this.roads = roads;
        setUpLights();
    }

    private void setUpLights() {
        roads.get(CompassDirection.NORTH).setGreenTrafficLight();
        roads.get(CompassDirection.SOUTH).setGreenTrafficLight();
    }

    public LightColor getCurrentTrafficLight(CompassDirection compassDirection) {
        return roads.get(compassDirection).getCurrentLightColor();
    }

    public void switchLights() {
        roads.values().forEach(Road::changeLightColor);
    }
}
