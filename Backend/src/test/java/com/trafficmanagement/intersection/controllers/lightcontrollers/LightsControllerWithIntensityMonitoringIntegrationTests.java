package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.trafficmanagement.intersection.constants.TrafficConfig.STARVATION_THRESHOLD;
import static com.trafficmanagement.intersection.constants.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LightsControllerWithIntensityMonitoringIntegrationTests {
    private LightsControllerWithIntensityMonitoring lightsControllerWithIntensityMonitoring;
    private Map<CompassDirection, Road> roads;

    @BeforeEach
    void setUp() {
        var southStraightTrafficLights = new TrafficLights();
        southStraightTrafficLights.setGreenLight();

        var northStraightTrafficLights = new TrafficLights();
        northStraightTrafficLights.setGreenLight();

        roads = Map.of(
                CompassDirection.NORTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), northStraightTrafficLights
                )),
                CompassDirection.EAST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.SOUTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), southStraightTrafficLights
                )),
                CompassDirection.WEST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                ))
        );
        lightsControllerWithIntensityMonitoring = new LightsControllerWithIntensityMonitoring(roads);
    }


    @Test
    void shouldNotSwitchLightsIfOtherRoadsAreEmpty() {
        int numberOfVehicles = 50;
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));

        addVehiclesToDirection(numberOfVehicles, CompassDirection.SOUTH, CompassDirection.NORTH);
        addVehiclesToDirection(numberOfVehicles, CompassDirection.NORTH, CompassDirection.SOUTH);

        while (numberOfVehicles > 0) {
            makeStepsToSwitchLights();
            assertEquals(LightColor.GREEN,
                    roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
            assertEquals(LightColor.GREEN,
                    roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
            numberOfVehicles -= 5;
        }
    }

    private void addVehiclesToDirection(int vehicleCount, CompassDirection startRoad, CompassDirection endRoad) {
        Road road = roads.get(startRoad);
        for (int i = 0; i < vehicleCount; i++) {
            Vehicle vehicle = new Vehicle(UUID.randomUUID().toString(), startRoad, endRoad);
            road.addVehicleToRoadLine(vehicle);
        }
    }

    private void makeStepsToSwitchLights() {
        for (int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH; i++) {
            lightsControllerWithIntensityMonitoring.makeStep();
        }
    }

    @Test
    void shouldSwitchLightsIfOtherLineIsWaitingToGoStraight() {
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));

        addVehiclesToDirection(1, CompassDirection.SOUTH, CompassDirection.NORTH);
        addVehiclesToDirection(50, CompassDirection.NORTH, CompassDirection.SOUTH);
        addVehiclesToDirection(50, CompassDirection.NORTH, CompassDirection.EAST);

        for (int i = 0; i < STARVATION_THRESHOLD; i += 5) {
            makeStepsToSwitchLights();
            assertEquals(LightColor.GREEN,
                    roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
            assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.LEFT)));
            assertEquals(LightColor.RED,
                    roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        }
        makeStepsToSwitchLights();

        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        assertEquals(LightColor.RED, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.LEFT)));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
    }

    @Test
    void shouldSwitchLightsIfOtherLineIsWaitingToGoLeft() {
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));

        addVehiclesToDirection(1, CompassDirection.NORTH, CompassDirection.EAST);
        addVehiclesToDirection(50, CompassDirection.SOUTH, CompassDirection.NORTH);
        addVehiclesToDirection(50, CompassDirection.SOUTH, CompassDirection.EAST);

        for (int i = 0; i < STARVATION_THRESHOLD; i += 5) {
            makeStepsToSwitchLights();
        }

        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.LEFT)));
        assertEquals(LightColor.RED, roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));

        var southLeft = roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.LEFT));
        var northStraightRight = roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT));
        assertTrue(
                (LightColor.GREEN == southLeft && LightColor.RED == northStraightRight) ||
                        (LightColor.RED == southLeft && LightColor.GREEN == northStraightRight)
        );
    }

    @Test
    void shouldSwitchLightsToStraightOnWestAndEast() {
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.SOUTH).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));

        addVehiclesToDirection(50, CompassDirection.EAST, CompassDirection.NORTH);
        addVehiclesToDirection(50, CompassDirection.WEST, CompassDirection.SOUTH);

        makeStepsToSwitchLights();

        assertEquals(LightColor.GREEN, roads.get(CompassDirection.EAST).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.WEST).getCurrentLightColor(Set.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)));
    }


}
