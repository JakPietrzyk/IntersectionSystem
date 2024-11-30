package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Intersection;
import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.models.IntersectionState;
import com.trafficmanagement.intersection.models.Vehicle;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/traffic")
@CrossOrigin(origins = "http://localhost:4200")
public class TrafficController {
    private Intersection intersection;

    public TrafficController() {
        this.intersection = initializeNewIntersection();
    }

    private static Intersection initializeNewIntersection() {
        return new Intersection(Map.of(
                CompassDirection.NORTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.EAST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.SOUTH, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                )),
                CompassDirection.WEST, new Road(Map.of(
                        new LeftTurnRoadLine(), new TrafficLights(),
                        new StraightOrRightRoadLine(), new TrafficLights()
                ))
        ));
    }

    @GetMapping("/restart")
    public void restartIntersection() {
        this.intersection = initializeNewIntersection();
    }

    @PostMapping("/step")
    public void executeStep() {
        intersection.step();
    }

    @GetMapping( "/state")
    public IntersectionState getIntersectionState() {
        return intersection.getIntersectionStatus();
    }

    @PostMapping("/addVehicle")
    public void addVehicle(@RequestBody List<Vehicle> vehicles) {
        vehicles.stream()
                .map(vehicle -> new Vehicle(vehicle.id(), vehicle.startRoad(), vehicle.turnDirection()))
                .forEach(intersection::addVehicle);
    }
}
