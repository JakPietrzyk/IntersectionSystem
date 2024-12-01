package com.trafficmanagement.intersection.components.roadlines;

import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.Vehicle;

import java.util.*;

public abstract class RoadLine {
    private final Queue<Vehicle> waitingVehicles = new LinkedList<>();
    private final EnumSet<TurnDirection> allowedDirections;

    protected RoadLine(EnumSet<TurnDirection> allowedDirections) {
        this.allowedDirections = allowedDirections;
    }

    public final void addVehicle(Vehicle vehicle) {
        waitingVehicles.add(vehicle);
    }

    public final Optional<Vehicle> removeFirstVehicle() {
        return Optional.ofNullable(waitingVehicles.poll());
    }

    public final int getVehicleCount() {
        return waitingVehicles.size();
    }

    public final boolean isEmpty() {
        return waitingVehicles.isEmpty();
    }

    public final Set<TurnDirection> getAllowedDirections() {
        return EnumSet.copyOf(allowedDirections);
    }

}
