package org.app.Intersection.Controllers;

import org.app.Intersection.Components.Road;
import org.app.Intersection.Components.TrafficLights;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.RoadLines.BasicRoadLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class LightsFlowControllerTests {
    private LightsFlowController lightsFlowController;
    private TrafficLightsSwitcher trafficLightsSwitcher;

    @BeforeEach
    public void setUp() {
        var roads = Map.of(
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
        this.trafficLightsSwitcher = new TrafficLightsSwitcher(roads);
        this.lightsFlowController = new LightsFlowController(this.trafficLightsSwitcher);
    }

    @Test
    public void shouldSwitchLightsAfterDefinedSteps() {
        Assertions.assertEquals(LightColor.GREEN, this.trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH, TurnDirection.STRAIGHT));

        for(int i = 0; i < TrafficConfigForTests.STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
            lightsFlowController.makeStep();
        }

        Assertions.assertEquals(LightColor.RED, this.trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
    }
}
