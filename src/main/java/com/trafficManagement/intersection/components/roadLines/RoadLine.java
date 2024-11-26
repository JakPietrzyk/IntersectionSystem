package com.trafficManagement.intersection.components.roadLines;

import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.models.Vehicle;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Queue;

public abstract class RoadLine {
    private final Queue<Vehicle> waitingVehicles = new LinkedList<>();
    private final EnumSet<TurnDirection> allowedDirections;

    protected RoadLine(EnumSet<TurnDirection> allowedDirections) {
        this.allowedDirections = allowedDirections;
    }

    public final void addVehicle(Vehicle vehicle) {
        waitingVehicles.add(vehicle);
    }

    public final void removeFirstVehicle() {
        waitingVehicles.poll();
    }

    public final int getVehicleCount() {
        return waitingVehicles.size();
    }

    public final int getVehicleCountForTurnDirection(TurnDirection turnDirection) {
        return (int) waitingVehicles.stream().filter(vehicle -> vehicle.turnDirection() == turnDirection).count();
    }

    public final boolean isEmpty() {
        return waitingVehicles.isEmpty();
    }

    public final Vehicle peekFirstVehicle() {
        return waitingVehicles.peek();
    }

    public final EnumSet<TurnDirection> getAllowedDirections() {
        return allowedDirections;
    }

    public final boolean canVehicleProceed(TurnDirection direction) {
        return allowedDirections.contains(direction);
    }
}
