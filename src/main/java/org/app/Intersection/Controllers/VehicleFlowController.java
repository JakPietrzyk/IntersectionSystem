package org.app.Intersection.Controllers;

import org.app.Intersection.Components.Road;
import org.app.Intersection.Components.TrafficLights;
import org.app.Intersection.RoadLines.RoadLine;

import java.util.Collection;
import java.util.Map;

public class VehicleFlowController {
    private final Collection<Road> roads;

    public VehicleFlowController(Collection<Road> roads) {
        this.roads = roads;
    }

    public void makeStep() {
        for (Road road : roads) {
            Map<RoadLine, TrafficLights> roadLineLights = road.getRoadLineLights();

            for (Map.Entry<RoadLine, TrafficLights> entry : roadLineLights.entrySet()) {
                RoadLine roadLine = entry.getKey();
                TrafficLights trafficLights = entry.getValue();

                if (trafficLights.isGreenLight() && !roadLine.isEmpty()) {
                    roadLine.removeFirstVehicle();
                }
            }
        }
    }

}
