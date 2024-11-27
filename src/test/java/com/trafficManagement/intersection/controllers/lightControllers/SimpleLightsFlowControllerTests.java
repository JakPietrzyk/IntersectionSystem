package com.trafficManagement.intersection.controllers.lightControllers;

import com.trafficManagement.intersection.components.Road;
import com.trafficManagement.intersection.components.TrafficLights;
import com.trafficManagement.intersection.components.roadLines.LeftTurnRoadLine;
import com.trafficManagement.intersection.components.roadLines.StraightOrRightRoadLine;
import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.LightColor;
import com.trafficManagement.intersection.constants.TurnDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static com.trafficManagement.intersection.controllers.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals(LightColor.GREEN, this.simpleLightsFlowController.getCurrentLightColor(CompassDirection.SOUTH, TurnDirection.STRAIGHT));

        for (int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
            simpleLightsFlowController.makeStep();
        }

        assertEquals(LightColor.RED, this.simpleLightsFlowController.getCurrentLightColor(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
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
                () -> new SimpleLightsFlowController(incompleteRoads)
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

        new SimpleLightsFlowController(completeRoads);
    }
}
