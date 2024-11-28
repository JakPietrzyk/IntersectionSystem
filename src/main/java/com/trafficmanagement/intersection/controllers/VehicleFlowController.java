package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;

import java.util.Collection;

public class VehicleFlowController {
    private final Collection<Road> roads;

    public VehicleFlowController(Collection<Road> roads) {
        this.roads = roads;
    }

    public void makeStep() {
        roads.stream()
                .map(Road::getRoadLineLights)
                .flatMap(roadLineWithLights -> roadLineWithLights.entrySet().stream())
                .filter(entry -> !entry.getKey().isEmpty() && entry.getValue().isGreenLight())
                .forEach(entry -> entry.getKey().removeFirstVehicle());
    }
}
