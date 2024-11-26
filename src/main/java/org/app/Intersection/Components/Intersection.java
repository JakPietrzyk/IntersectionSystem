package org.app.Intersection.Components;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Controllers.LightControllers.LightsController;
import org.app.Intersection.Controllers.LightControllers.SimpleLightsFlowController;
import org.app.Intersection.Controllers.VehicleFlowController;
import org.app.Intersection.Models.Vehicle;

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
        roads.get(vehicle.getStartRoad()).addVehicleToRoadLine(vehicle);
    }

    public int getRoadVehicleCount(CompassDirection compassDirection, TurnDirection turnDirection) {
        return roads.get(compassDirection).getVehicleCount(turnDirection);
    }
}
