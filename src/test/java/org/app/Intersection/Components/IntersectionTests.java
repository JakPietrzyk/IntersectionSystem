package org.app.Intersection.Components;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Models.Vehicle;
import org.app.Intersection.RoadLines.BasicRoadLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.app.Intersection.Controllers.TrafficConfigForTests.STEPS_BEFORE_LIGHTS_SWITCH;

public class IntersectionTests {
    Map<CompassDirection, Road> roads;
    Intersection intersection;
    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
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

        Assertions.assertEquals(1, intersection.getRoadVehicleCount(CompassDirection.WEST, TurnDirection.STRAIGHT));

        makeStepsToSwitchLights();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.WEST, TurnDirection.STRAIGHT));
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

        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.SOUTH, TurnDirection.STRAIGHT));
    }
}
