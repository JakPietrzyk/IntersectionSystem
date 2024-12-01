package com.trafficmanagement.intersection.components.roadlines;

import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.Vehicle;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class RoadLine {
    private final Queue<Vehicle> waitingVehicles = new LinkedList<>();
    private final EnumSet<TurnDirection> allowedDirections;

    protected RoadLine(EnumSet<TurnDirection> allowedDirections) {
        this.allowedDirections = allowedDirections;
    }

    public final void addVehicle(Vehicle vehicle) {
        waitingVehicles.add(vehicle);
    }

    public final String removeFirstVehicle() {
        return waitingVehicles.poll().id();
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

    public final Set<TurnDirection> getAllowedDirections() {
        return EnumSet.copyOf(allowedDirections);
    }

    public final boolean canVehicleProceed(TurnDirection direction) {
        return allowedDirections.contains(direction);
    }
}
