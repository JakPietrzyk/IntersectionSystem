package org.app.Intersection.Components;

import org.app.Intersection.Constants.Direction;
import org.app.Intersection.Controllers.TrafficLightsSwitcher;
import org.app.Intersection.Controllers.VehicleFlowController;
import org.app.Intersection.Models.Vehicle;

import java.util.Map;

public class Intersection {
    private final Map<Direction, Road> roads;
    private final TrafficLightsSwitcher trafficLightsSwitcher;
    private final VehicleFlowController vehicleFlowController;

    public Intersection(Map<Direction, Road> roads) {
        this.trafficLightsSwitcher = new TrafficLightsSwitcher(roads);
        this.vehicleFlowController = new VehicleFlowController(roads.values());
        this.roads = roads;
    }

    public void step() {
        vehicleFlowController.makeStep();
    }

    public void addVehicle(Vehicle vehicle) {
        roads.get(vehicle.startRoad()).addVehicleToRoadLine(vehicle);
    }

    public int getRoadVehicleCount(Direction direction) {
        return roads.get(direction).getVehicleCount();
    }

    public void switchTrafficLights() {
        trafficLightsSwitcher.switchLights();
    }
}
