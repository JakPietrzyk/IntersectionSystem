package org.app.Intersection.Controllers;

import org.app.Intersection.Components.Road;

import java.util.Collection;

public class VehicleFlowController {
    private final Collection<Road> roads;

    public VehicleFlowController(Collection<Road> roads) {
        this.roads = roads;
    }

    public void makeStep() {
        for (Road road : roads) {
            if(road.isGreenTrafficLight()) {
                road.removeVehicleFromRoadLine();
            }
        }
    }
}
