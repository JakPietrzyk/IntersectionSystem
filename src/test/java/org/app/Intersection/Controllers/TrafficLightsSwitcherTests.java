package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Components.Road;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TrafficLightsSwitcherTests {
    Map<CompassDirection, Road> roads;

    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
                CompassDirection.NORTH, new Road(),
                CompassDirection.EAST, new Road(),
                CompassDirection.SOUTH, new Road(),
                CompassDirection.WEST, new Road()
        );
    }

    @Test
    public void shouldSetUpLights() {
        roads.values().forEach(road -> Assertions.assertFalse(road.isGreenTrafficLight(), "Traffic light should initially be red"));

        var trafficLightsSwitcher = new TrafficLightsSwitcher(roads);

        Assertions.assertEquals(LightColor.GREEN, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.NORTH), "North should have green light");
        Assertions.assertEquals(LightColor.GREEN, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH), "South should have green light");
        Assertions.assertEquals(LightColor.RED, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.EAST), "East should have red light");
        Assertions.assertEquals(LightColor.RED, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.WEST), "West should have red light");
    }


}
