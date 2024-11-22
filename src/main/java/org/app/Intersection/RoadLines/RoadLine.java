package org.app.Intersection.RoadLines;

import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Models.Vehicle;

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
