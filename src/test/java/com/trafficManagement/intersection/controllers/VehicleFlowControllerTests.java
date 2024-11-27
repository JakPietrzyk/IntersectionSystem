package com.trafficManagement.intersection.controllers;

import com.trafficManagement.intersection.components.Road;
import com.trafficManagement.intersection.components.TrafficLights;
import com.trafficManagement.intersection.components.roadLines.LeftTurnRoadLine;
import com.trafficManagement.intersection.components.roadLines.StraightOrRightRoadLine;
import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.models.Vehicle;
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
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
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
