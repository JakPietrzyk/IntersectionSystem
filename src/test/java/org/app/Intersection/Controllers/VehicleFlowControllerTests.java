package org.app.Intersection.Controllers;

import org.app.Intersection.Components.TrafficLights;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Components.Road;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Models.Vehicle;
import org.app.Intersection.RoadLines.BasicRoadLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class VehicleFlowControllerTests {
    private List<Road> roads;

    @BeforeEach
    public void setUp() {
        this.roads = List.of(
                new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                )),
                new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                )),
                new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                )),
                new Road(Map.of(
                        new BasicRoadLine(), new TrafficLights()
                ))
        );
    }

    @Test
    public void shouldRemoveCarWhenGreenLight() {
        roads.getFirst().addVehicleToRoadLine(new Vehicle("1", CompassDirection.NORTH, CompassDirection.SOUTH));
        roads.getFirst().setGreenTrafficLight(TurnDirection.STRAIGHT);
        var vehicleFlowController = new VehicleFlowController(roads);
        vehicleFlowController.makeStep();

        Assertions.assertEquals(0, roads.getFirst().getVehicleCount(TurnDirection.STRAIGHT));
    }

    @Test
    public void shouldNotRemoveCarWhenRedLight() {
        roads.getFirst().addVehicleToRoadLine(new Vehicle("1", CompassDirection.NORTH, CompassDirection.SOUTH));
        var vehicleFlowController = new VehicleFlowController(roads);
        vehicleFlowController.makeStep();

        Assertions.assertEquals(1, roads.getFirst().getVehicleCount(TurnDirection.STRAIGHT));
    }

}
