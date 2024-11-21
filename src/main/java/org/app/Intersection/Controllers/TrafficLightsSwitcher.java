package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.Direction;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Components.Road;

import java.util.Map;

public class TrafficLightsSwitcher {
    private final Map<Direction, Road> roads;

    public TrafficLightsSwitcher(Map<Direction, Road> roads) {
        this.roads = roads;
        setUpLights();
    }

    private void setUpLights() {
        roads.get(Direction.NORTH).setGreenTrafficLight();
        roads.get(Direction.SOUTH).setGreenTrafficLight();
    }

    public LightColor getCurrentTrafficLight(Direction direction) {
        return roads.get(direction).getCurrentLightColor();
    }

    public void switchLights() {
        roads.values().forEach(Road::changeLightColor);
    }
}
