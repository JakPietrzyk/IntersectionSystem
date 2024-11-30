package com.trafficmanagement.intersection.components;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.VehicleFlowController;
import com.trafficmanagement.intersection.controllers.lightcontrollers.LightsController;
import com.trafficmanagement.intersection.controllers.lightcontrollers.SimpleLightsFlowController;
import com.trafficmanagement.intersection.models.IntersectionState;
import com.trafficmanagement.intersection.models.Vehicle;

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

    public IntersectionState getIntersectionStatus() {
        return new IntersectionState(this.roads);
    }
}
