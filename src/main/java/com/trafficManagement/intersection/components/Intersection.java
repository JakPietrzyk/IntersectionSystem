package com.trafficManagement.intersection.components;

import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.controllers.VehicleFlowController;
import com.trafficManagement.intersection.controllers.lightControllers.LightsController;
import com.trafficManagement.intersection.controllers.lightControllers.SimpleLightsFlowController;
import com.trafficManagement.intersection.models.Vehicle;

import java.util.Map;

public class Intersection {
    private final Map<CompassDirection, Road> roads;
    private final VehicleFlowController vehicleFlowController;
    private final LightsController simpleLightsFlowController;

    public Intersection(Map<CompassDirection, Road> roads) {
        this.vehicleFlowController = new VehicleFlowController(roads.values());
        this.simpleLightsFlowController = new SimpleLightsFlowController(roads);
        this.roads = roads;
    }

    public void step() {
        vehicleFlowController.makeStep();
        simpleLightsFlowController.makeStep();
    }

    public void addVehicle(Vehicle vehicle) {
        roads.get(vehicle.startRoad()).addVehicleToRoadLine(vehicle);
    }

    public int getRoadVehicleCount(CompassDirection compassDirection, TurnDirection turnDirection) {
        return roads.get(compassDirection).getVehicleCount(turnDirection);
    }
}
