package com.trafficmanagement.intersection.controllers.restApi;

import com.trafficmanagement.intersection.components.intersections.Intersection;
import com.trafficmanagement.intersection.models.Vehicle;
import com.trafficmanagement.intersection.models.statuses.IntersectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traffic")
@CrossOrigin(origins = "http://localhost:4200")
public class TrafficController {
    private final ApplicationContext applicationContext;
    private Intersection intersection;

    @Autowired
    public TrafficController(Intersection intersection, ApplicationContext applicationContext) {
        this.intersection = intersection;
        this.applicationContext = applicationContext;
    }

    private Intersection initializeNewIntersection() {
        return applicationContext.getBean(Intersection.class);
    }

    @GetMapping("/restart")
    public void restartIntersection() {
        this.intersection = initializeNewIntersection();
    }

    @PostMapping("/step")
    public void executeStep() {
        intersection.step();
    }

    @GetMapping("/state")
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
