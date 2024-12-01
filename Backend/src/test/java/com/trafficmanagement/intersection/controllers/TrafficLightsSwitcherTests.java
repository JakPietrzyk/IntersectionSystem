package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class TrafficLightsSwitcherTests {
    Map<CompassDirection, Road> roads;

    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
                CompassDirection.NORTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.EAST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.SOUTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.WEST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                ))
        );
    }

    @Test
    void shouldSetUpLights() {
        roads.values().forEach(road -> Assertions.assertFalse(road.isGreenTrafficLight(TurnDirection.STRAIGHT)));

        var trafficLightsSwitcher = new TrafficLightsSwitcher(roads);

        Assertions.assertEquals(LightColor.GREEN,
                trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.NORTH, TurnDirection.STRAIGHT));
        Assertions.assertEquals(LightColor.GREEN,
                trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
        Assertions.assertEquals(LightColor.RED,
                trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.EAST, TurnDirection.STRAIGHT));
        Assertions.assertEquals(LightColor.RED,
                trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.WEST, TurnDirection.STRAIGHT));
    }
}
