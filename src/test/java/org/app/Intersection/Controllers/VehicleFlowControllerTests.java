package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.Direction;
import org.app.Intersection.Components.Road;
import org.app.Intersection.Models.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VehicleFlowControllerTests {
    private List<Road> roads;

    @BeforeEach
    public void setUp() {
        this.roads = List.of(
                 new Road(),
                 new Road(),
                 new Road(),
                 new Road()
        );
    }

    @Test
    public void shouldRemoveCarWhenGreenLight() {
        roads.getFirst().addVehicleToRoadLine(new Vehicle("1", Direction.NORTH, Direction.SOUTH));
        roads.getFirst().setGreenTrafficLight();
        var vehicleFlowController = new VehicleFlowController(roads);
        vehicleFlowController.makeStep();

        Assertions.assertEquals(0, roads.getFirst().getVehicleCount());
    }

    @Test
    public void shouldNotRemoveCarWhenRedLight() {
        roads.getFirst().addVehicleToRoadLine(new Vehicle("1", Direction.NORTH, Direction.SOUTH));
        var vehicleFlowController = new VehicleFlowController(roads);
        vehicleFlowController.makeStep();

        Assertions.assertEquals(1, roads.getFirst().getVehicleCount());
    }

}
