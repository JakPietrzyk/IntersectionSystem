package com.trafficmanagement.intersection.models;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.helpers.TurnDirectionPicker;

public record Vehicle(
        String id,
        CompassDirection startRoad,
        CompassDirection endRoad,
        TurnDirection turnDirection
) {
    public Vehicle(String id, CompassDirection startRoad, CompassDirection endRoad) {
        this(id, startRoad, endRoad, TurnDirectionPicker.getTurnDirection(startRoad, endRoad));
    }

    public Vehicle(String id, CompassDirection startRoad, TurnDirection turnDirection) {
        this(id, startRoad, TurnDirectionPicker.getEndRoad(startRoad, turnDirection), turnDirection);
    }
}
