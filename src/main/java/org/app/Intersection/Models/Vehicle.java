package org.app.Intersection.Models;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Helpers.TurnDirectionPicker;

public final class Vehicle {
    private final String id;
    private final CompassDirection startRoad;
    private final CompassDirection endRoad;
    private final TurnDirection turnDirection;

    public Vehicle(String id, CompassDirection startRoad, CompassDirection endRoad) {
        this.id = id;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
        this.turnDirection = TurnDirectionPicker.getTurnDirection(startRoad, endRoad);
    }

    public CompassDirection getStartRoad() {
        return startRoad;
    }

    public TurnDirection getTurnDirection() {
        return turnDirection;
    }
}
