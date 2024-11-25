package org.app.Intersection.Components;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Models.Vehicle;
import org.app.Intersection.RoadLines.LeftTurnRoadLine;
import org.app.Intersection.RoadLines.StraightOrRightRoadLine;
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
