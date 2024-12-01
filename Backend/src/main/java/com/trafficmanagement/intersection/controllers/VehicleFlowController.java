package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.models.Vehicle;
import com.trafficmanagement.intersection.models.statuses.StepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VehicleFlowController {
    private final Collection<Road> roads;
    private final List<StepStatus> stepStatuses;
    private final Logger logger = LoggerFactory.getLogger(VehicleFlowController.class);

    public VehicleFlowController(Collection<Road> roads) {
        this.roads = roads;
        this.stepStatuses = new ArrayList<>();
    }

    public void makeStep() {
        List<Vehicle> removedVehicles = roads.stream()
                .map(Road::getRoadLineLights)
                .flatMap(roadLineWithLights -> roadLineWithLights.entrySet().stream())
                .filter(entry -> !entry.getKey().isEmpty() && entry.getValue().isGreenLight())
                .flatMap(entry -> entry.getKey().removeFirstVehicle().stream())
                .toList();


        removedVehicles.forEach(
                removedVehicle -> logger.info("Vehicle with id: {} from direction: {} , to direction: {} was removed",
                        removedVehicle.id(),
                        removedVehicle.startRoad(), removedVehicle.endRoad()));
        this.stepStatuses.add(new StepStatus(removedVehicles.stream().map(Vehicle::id).toList()));
    }

    public List<StepStatus> getStepStatuses() {
        return List.copyOf(stepStatuses);
    }
}
