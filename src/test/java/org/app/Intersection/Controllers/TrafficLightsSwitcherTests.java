package org.app.Intersection.Controllers;

import org.app.Intersection.Components.TrafficLights;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Components.Road;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.RoadLines.BasicRoadLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TrafficLightsSwitcherTests {
    Map<CompassDirection, Road> roads;

    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
                CompassDirection.NORTH, new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                )),
                CompassDirection.EAST, new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                )),
                CompassDirection.SOUTH, new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                )),
                CompassDirection.WEST, new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                ))
        );
    }

    @Test
    public void shouldSetUpLights() {
        roads.values().forEach(road -> Assertions.assertFalse(road.isGreenTrafficLight(TurnDirection.STRAIGHT)));

        var trafficLightsSwitcher = new TrafficLightsSwitcher(roads);

        Assertions.assertEquals(LightColor.GREEN, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.NORTH, TurnDirection.STRAIGHT));
        Assertions.assertEquals(LightColor.GREEN, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
        Assertions.assertEquals(LightColor.RED, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.EAST, TurnDirection.STRAIGHT));
        Assertions.assertEquals(LightColor.RED, trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.WEST, TurnDirection.STRAIGHT));
    }


}
