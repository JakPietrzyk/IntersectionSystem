package com.trafficManagement.intersection.models;

import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.helpers.TurnDirectionPicker;

public record Vehicle(
        String id,
        CompassDirection startRoad,
        CompassDirection endRoad,
        TurnDirection turnDirection
) {
    public Vehicle(String id, CompassDirection startRoad, CompassDirection endRoad) {
        this(id, startRoad, endRoad, TurnDirectionPicker.getTurnDirection(startRoad, endRoad));
    }
}
