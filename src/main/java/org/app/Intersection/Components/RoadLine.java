package org.app.Intersection.Components;

import org.app.Intersection.Models.Vehicle;

import java.util.LinkedList;
import java.util.Queue;

public class RoadLine {
    private final Queue<Vehicle> waitingVehicles = new LinkedList<>();

    public void addVehicle(Vehicle vehicle) {
        waitingVehicles.add(vehicle);
    }

    public void removeFirstVehicle() {
        waitingVehicles.poll();
    }

    public int getVehicleCount() {
        return waitingVehicles.size();
    }
}
