package org.app.Intersection.Components;

import org.app.Intersection.Constants.Direction;
import org.app.Intersection.Models.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IntersectionTests {
    Map<Direction, Road> roads;
    Intersection intersection;
    @BeforeEach
    public void setUp() {
        this.roads = Map.of(
                Direction.NORTH, new Road(),
                Direction.EAST, new Road(),
                Direction.SOUTH, new Road(),
                Direction.WEST, new Road()
        );
        this.intersection = new Intersection(roads);
    }

    @Test
    void shouldVehiclePass() {
        intersection.addVehicle(new Vehicle("1", Direction.SOUTH, Direction.NORTH));
        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(Direction.SOUTH));
    }

    @Test
    void shouldVehiclePassAfterLightSwitch() {
        intersection.addVehicle(new Vehicle("1", Direction.WEST, Direction.NORTH));
        intersection.step();

        Assertions.assertEquals(1, intersection.getRoadVehicleCount(Direction.WEST));

        intersection.switchTrafficLights();
        intersection.step();

        Assertions.assertEquals(0, intersection.getRoadVehicleCount(Direction.WEST));
    }

}
