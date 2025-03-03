package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class VehicleFlowControllerTests {
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
    void shouldRemoveCarWhenGreenLight() {
        roads.getFirst().addVehicleToRoadLine(new Vehicle("1", CompassDirection.NORTH, CompassDirection.SOUTH));
        roads.getFirst().setGreenTrafficLight(TurnDirection.STRAIGHT);
        var vehicleFlowController = new VehicleFlowController(roads);
        vehicleFlowController.makeStep();

        Assertions.assertEquals(0, roads.getFirst().getVehicleCount(TurnDirection.STRAIGHT));
    }

    @Test
    void shouldNotRemoveCarWhenRedLight() {
        roads.getFirst().addVehicleToRoadLine(new Vehicle("1", CompassDirection.NORTH, CompassDirection.SOUTH));
        var vehicleFlowController = new VehicleFlowController(roads);
        vehicleFlowController.makeStep();

        Assertions.assertEquals(1, roads.getFirst().getVehicleCount(TurnDirection.STRAIGHT));
    }

}
