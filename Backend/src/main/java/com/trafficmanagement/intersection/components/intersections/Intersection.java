package com.trafficmanagement.intersection.components.intersections;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.VehicleFlowController;
import com.trafficmanagement.intersection.controllers.lightcontrollers.LightsController;
import com.trafficmanagement.intersection.models.Vehicle;
import com.trafficmanagement.intersection.models.statuses.IntersectionState;
import com.trafficmanagement.intersection.models.statuses.StepStatus;

import java.util.List;
import java.util.Map;

public abstract class Intersection {
    private final Map<CompassDirection, Road> roads;
    private final VehicleFlowController vehicleFlowController;
    private final LightsController lightsController;

    protected Intersection(Map<CompassDirection, Road> roads, LightsController lightsController) {
        this.vehicleFlowController = new VehicleFlowController(roads.values());
        this.lightsController = lightsController;
        this.roads = roads;
    }

    public void step() {
        vehicleFlowController.makeStep();
        lightsController.makeStep();
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

    public List<StepStatus> getStepStatuses() {
        return vehicleFlowController.getStepStatuses();
    }
}
