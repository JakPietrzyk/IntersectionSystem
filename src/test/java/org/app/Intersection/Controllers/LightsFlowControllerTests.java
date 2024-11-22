package org.app.Intersection.Controllers;

import org.app.Intersection.Components.Road;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class LightsFlowControllerTests {
    private LightsFlowController lightsFlowController;
    private TrafficLightsSwitcher trafficLightsSwitcher;
    private final int STEPS_BEFORE_LIGHTS_SWITCH = 5;

    @BeforeEach
    public void setUp() {
        var roads = Map.of(
                CompassDirection.NORTH, new Road(),
                CompassDirection.EAST, new Road(),
                CompassDirection.SOUTH, new Road(),
                CompassDirection.WEST, new Road()
        );
        this.trafficLightsSwitcher = new TrafficLightsSwitcher(roads);
        this.lightsFlowController = new LightsFlowController(this.trafficLightsSwitcher);
    }

    @Test
    public void shouldSwitchLightsAfterDefinedSteps() {
        Assertions.assertEquals(LightColor.GREEN, this.trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH));

        for(int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
            lightsFlowController.makeStep();
        }

        Assertions.assertEquals(LightColor.RED, this.trafficLightsSwitcher.getCurrentTrafficLight(CompassDirection.SOUTH));
    }
}
