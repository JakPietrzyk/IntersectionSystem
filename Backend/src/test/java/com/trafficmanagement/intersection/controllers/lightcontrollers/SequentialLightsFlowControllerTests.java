package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static com.trafficmanagement.intersection.constants.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;
import static org.junit.jupiter.api.Assertions.*;

class SequentialLightsFlowControllerTests {
    private SequentialLightsFlowController sequentialLightsFlowController;

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
        this.sequentialLightsFlowController = new SequentialLightsFlowController(roads);
    }

    @Test
    void shouldSwitchLightsAfterDefinedSteps() {
        assertEquals(LightColor.GREEN, this.sequentialLightsFlowController.getCurrentLightColor(CompassDirection.SOUTH,
                TurnDirection.STRAIGHT));

        for (int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
            sequentialLightsFlowController.makeStep();
        }

        assertEquals(LightColor.RED, this.sequentialLightsFlowController.getCurrentLightColor(CompassDirection.SOUTH,
                TurnDirection.STRAIGHT));
    }

    @Test
    void shouldThrowExceptionIfRoadsMapIsMissingDirections() {
        var incompleteRoads = Map.of(
                CompassDirection.NORTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.SOUTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new SequentialLightsFlowController(incompleteRoads)
        );
    }

    @Test
    void shouldNotThrowExceptionIfRoadsMapContainsAllDirections() {
        Map<CompassDirection, Road> completeRoads = new EnumMap<>(CompassDirection.class);
        for (CompassDirection direction : CompassDirection.values()) {
            completeRoads.put(direction, new Road(Map.of(
                    new LeftTurnRoadLine(), new TrafficLights(),
                    new StraightOrRightRoadLine(), new TrafficLights()
            )));
        }

        assertDoesNotThrow(() -> new SequentialLightsFlowController(completeRoads));
    }
}
