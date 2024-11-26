package org.app.Intersection.Controllers.LightControllers;

import org.app.Intersection.Components.Road;
import org.app.Intersection.Components.TrafficLights;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.RoadLines.LeftTurnRoadLine;
import org.app.Intersection.RoadLines.StraightOrRightRoadLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.app.Intersection.Controllers.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;

class SimpleLightsFlowControllerTests {
    private SimpleLightsFlowController simpleLightsFlowController;

    @BeforeEach
    public void setUp() {
        var roads = Map.of(
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
        this.simpleLightsFlowController = new SimpleLightsFlowController(roads);
    }

    @Test
    void shouldSwitchLightsAfterDefinedSteps() {
        Assertions.assertEquals(LightColor.GREEN, this.simpleLightsFlowController.getCurrentLightColor(CompassDirection.SOUTH, TurnDirection.STRAIGHT));

        for(int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
            simpleLightsFlowController.makeStep();
        }

        Assertions.assertEquals(LightColor.RED, this.simpleLightsFlowController.getCurrentLightColor(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
    }
}
