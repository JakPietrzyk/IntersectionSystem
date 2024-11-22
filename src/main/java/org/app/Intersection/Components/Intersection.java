package org.app.Intersection.Components;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Controllers.LightsFlowController;
import org.app.Intersection.Controllers.TrafficLightsSwitcher;
import org.app.Intersection.Controllers.VehicleFlowController;
import org.app.Intersection.Models.Vehicle;

import java.util.Map;

public class Intersection {
    private final Map<CompassDirection, Road> roads;
    private final VehicleFlowController vehicleFlowController;
    private final LightsFlowController lightsFlowController;

    public Intersection(Map<CompassDirection, Road> roads) {
        this.vehicleFlowController = new VehicleFlowController(roads.values());
        this.lightsFlowController = new LightsFlowController(new TrafficLightsSwitcher(roads));
        this.roads = roads;
    }

    public void step() {
        vehicleFlowController.makeStep();
        lightsFlowController.makeStep();
    }

    public void addVehicle(Vehicle vehicle) {
        roads.get(vehicle.getStartRoad()).addVehicleToRoadLine(vehicle);
    }

    public int getRoadVehicleCount(CompassDirection compassDirection) {
        return roads.get(compassDirection).getVehicleCount();
    }
}
