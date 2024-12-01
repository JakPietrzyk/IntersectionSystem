package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.models.statuses.StepStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VehicleFlowController {
    private final Collection<Road> roads;
    private final List<StepStatus> stepStatuses;

    public VehicleFlowController(Collection<Road> roads) {
        this.roads = roads;
        this.stepStatuses = new ArrayList<>();
    }

    public void makeStep() {
        List<String> removedVehicles = roads.stream()
                .map(Road::getRoadLineLights)
                .flatMap(roadLineWithLights -> roadLineWithLights.entrySet().stream())
                .filter(entry -> !entry.getKey().isEmpty() && entry.getValue().isGreenLight())
                .map(entry -> entry.getKey().removeFirstVehicle())
                .toList();

        this.stepStatuses.add(new StepStatus(removedVehicles));
    }

    public List<StepStatus> getStepStatuses() {
        return List.copyOf(stepStatuses);
    }
}
