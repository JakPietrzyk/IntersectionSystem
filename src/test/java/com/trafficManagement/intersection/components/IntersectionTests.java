package com.trafficManagement.intersection.components;

import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.models.Vehicle;
import com.trafficManagement.intersection.components.roadLines.LeftTurnRoadLine;
import com.trafficManagement.intersection.components.roadLines.StraightOrRightRoadLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.trafficManagement.intersection.controllers.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;

class IntersectionTests {
    Map<CompassDirection, Road> roads;
    Intersection intersection;
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
        this.intersection = new Intersection(roads);
    }

    @Test
    void shouldVehiclePass() {
        intersection.addVehicle(new Vehicle("1", CompassDirection.SOUTH, CompassDirection.NORTH));
        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
    }

    @Test
    void shouldVehiclePassAfterLightSwitch() {
        intersection.addVehicle(new Vehicle("1", CompassDirection.WEST, CompassDirection.NORTH));
        intersection.step();

        Assertions.assertEquals(1, intersection.getRoadVehicleCount(CompassDirection.WEST, TurnDirection.LEFT));

        makeStepsToSwitchLights();
        makeStepsToSwitchLights();
        makeStepsToSwitchLights();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.WEST, TurnDirection.LEFT));
    }

    private void makeStepsToSwitchLights() {
        for(int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
            intersection.step();
        }
    }

    @Test
    void shouldIncomingVehiclePassAfterLightSwitch() {
        makeStepsToSwitchLights();

        intersection.addVehicle(new Vehicle("1", CompassDirection.SOUTH, CompassDirection.NORTH));

        Assertions.assertEquals(1, intersection.getRoadVehicleCount(CompassDirection.SOUTH, TurnDirection.STRAIGHT));

        makeStepsToSwitchLights();
        makeStepsToSwitchLights();
        makeStepsToSwitchLights();

        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
    }
}
