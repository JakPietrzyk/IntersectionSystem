package com.trafficmanagement.intersection.components;

import com.trafficmanagement.intersection.components.intersections.IntensityHandlingIntersection;
import com.trafficmanagement.intersection.components.intersections.Intersection;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.trafficmanagement.intersection.constants.TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH;

class IntersectionTests {
    Map<CompassDirection, Road> roads;
    Intersection intersection;

    @BeforeEach
    public void setUp() {
        var southStraightTrafficLights = new TrafficLights();
        southStraightTrafficLights.setGreenLight();

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
                        new StraightOrRightRoadLine(), southStraightTrafficLights
                )),
                CompassDirection.WEST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                ))
        );
        this.intersection = new IntensityHandlingIntersection(roads);
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
        for (int i = 0; i < STEPS_BEFORE_LIGHTS_SWITCH + 1; i++) {
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
