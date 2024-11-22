package org.app.Intersection.Components;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Models.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IntersectionTests {
    private final int STEPS_BEFORE_LIGHTS_SWITCH = 5;
    Map<CompassDirection, Road> roads;
    Intersection intersection;
    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
                CompassDirection.NORTH, new Road(),
                CompassDirection.EAST, new Road(),
                CompassDirection.SOUTH, new Road(),
                CompassDirection.WEST, new Road()
        );
        this.intersection = new Intersection(roads);
    }

    @Test
    void shouldVehiclePass() {
        intersection.addVehicle(new Vehicle("1", CompassDirection.SOUTH, CompassDirection.NORTH));
        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.SOUTH));
    }

    @Test
    void shouldVehiclePassAfterLightSwitch() {
        intersection.addVehicle(new Vehicle("1", CompassDirection.WEST, CompassDirection.NORTH));
        intersection.step();

        Assertions.assertEquals(1, intersection.getRoadVehicleCount(CompassDirection.WEST));

        makeStepsToSwitchLights();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.WEST));
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

        Assertions.assertEquals(1, intersection.getRoadVehicleCount(CompassDirection.SOUTH));

        makeStepsToSwitchLights();

        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(CompassDirection.SOUTH));
    }
}
