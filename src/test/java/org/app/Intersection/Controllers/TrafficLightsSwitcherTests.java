package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.Direction;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Components.Road;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TrafficLightsSwitcherTests {
    Map<Direction, Road> roads;

    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
                Direction.NORTH, new Road(),
                Direction.EAST, new Road(),
                Direction.SOUTH, new Road(),
                Direction.WEST, new Road()
        );
    }

    @Test
    public void shouldSetUpLights() {
        roads.values().forEach(road -> Assertions.assertFalse(road.isGreenTrafficLight(), "Traffic light should initially be red"));

        var trafficLightsSwitcher = new TrafficLightsSwitcher(roads);

        Assertions.assertEquals(LightColor.GREEN, trafficLightsSwitcher.getCurrentTrafficLight(Direction.NORTH), "North should have green light");
        Assertions.assertEquals(LightColor.GREEN, trafficLightsSwitcher.getCurrentTrafficLight(Direction.SOUTH), "South should have green light");
        Assertions.assertEquals(LightColor.RED, trafficLightsSwitcher.getCurrentTrafficLight(Direction.EAST), "East should have red light");
        Assertions.assertEquals(LightColor.RED, trafficLightsSwitcher.getCurrentTrafficLight(Direction.WEST), "West should have red light");
    }


}
