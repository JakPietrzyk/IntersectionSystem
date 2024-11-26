package org.app.Intersection.Controllers.LightControllers;

import org.app.Intersection.Components.Road;
import org.app.Intersection.Components.TrafficLights;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Models.Vehicle;
import org.app.Intersection.RoadLines.LeftTurnRoadLine;
import org.app.Intersection.RoadLines.StraightOrRightRoadLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.app.Intersection.Controllers.TrafficConfig.STARVATION_THRESHOLD;
import static org.app.Intersection.Controllers.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;
import static org.junit.jupiter.api.Assertions.*;


class LightsControllerWithIntensityMonitoringTests {
    private LightsControllerWithIntensityMonitoring lightsControllerWithIntensityMonitoring;
    private Map<CompassDirection, Road> roads;

    @BeforeEach
    void setUp() {
        roads = Map.of(
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
        lightsControllerWithIntensityMonitoring = new LightsControllerWithIntensityMonitoring(roads);
    }


    @Test
    void shouldNotSwitchLightsIfOtherRoadsAreEmpty() {
        int numberOfVehicles = 50;
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));

        addVehiclesToDirection(numberOfVehicles, CompassDirection.SOUTH, CompassDirection.NORTH);
        addVehiclesToDirection(numberOfVehicles, CompassDirection.NORTH, CompassDirection.SOUTH);

        while(numberOfVehicles > 0) {
            makeStepsToSwitchLights();
            assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
            assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));
            numberOfVehicles -= 5;
        }
    }

    private void addVehiclesToDirection(int vehicleCount, CompassDirection startRoad, CompassDirection endRoad) {
        Road road = roads.get(startRoad);
        for(int i = 0; i < vehicleCount; i++) {
            Vehicle vehicle = new Vehicle(UUID.randomUUID().toString(), startRoad, endRoad);
            road.addVehicleToRoadLine(vehicle);
        }
    }

    private void makeStepsToSwitchLights() {
        for(int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH; i++) {
            lightsControllerWithIntensityMonitoring.makeStep();
        }
    }

    @Test
    void shouldSwitchLightsIfOtherLineIsWaitingToGoStraight() {
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));

        addVehiclesToDirection(1, CompassDirection.SOUTH, CompassDirection.NORTH);
        addVehiclesToDirection(50, CompassDirection.NORTH, CompassDirection.SOUTH);
        addVehiclesToDirection(50, CompassDirection.NORTH, CompassDirection.EAST);

        for(int i = 0; i < STARVATION_THRESHOLD; i+=5) {
            makeStepsToSwitchLights();
            assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
            assertEquals(LightColor.GREEN , roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.LEFT));
            assertEquals(LightColor.RED , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        }
        makeStepsToSwitchLights();

        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.RED , roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.LEFT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));
    }

    @Test
    void shouldSwitchLightsIfOtherLineIsWaitingToGoLeft() {
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));

        addVehiclesToDirection(1, CompassDirection.NORTH, CompassDirection.EAST);
        addVehiclesToDirection(50, CompassDirection.SOUTH, CompassDirection.NORTH);
        addVehiclesToDirection(50, CompassDirection.SOUTH, CompassDirection.WEST);

        for(int i = 0; i < STARVATION_THRESHOLD; i+=5) {
            makeStepsToSwitchLights();
        }

        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.LEFT));
        assertEquals(LightColor.RED , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.LEFT));
    }

    @Test
    void shouldSwitchLightsToStraightOnWestAndEast() {
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.NORTH).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.SOUTH).getCurrentLightColor(TurnDirection.STRAIGHT));

        addVehiclesToDirection(50, CompassDirection.EAST, CompassDirection.NORTH);
        addVehiclesToDirection(50, CompassDirection.WEST, CompassDirection.SOUTH);

        makeStepsToSwitchLights();

        assertEquals(LightColor.GREEN, roads.get(CompassDirection.EAST).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN, roads.get(CompassDirection.EAST).getCurrentLightColor(TurnDirection.RIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.WEST).getCurrentLightColor(TurnDirection.STRAIGHT));
        assertEquals(LightColor.GREEN , roads.get(CompassDirection.WEST).getCurrentLightColor(TurnDirection.RIGHT));
    }


}
