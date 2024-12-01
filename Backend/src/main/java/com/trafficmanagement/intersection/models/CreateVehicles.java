package com.trafficmanagement.intersection.models;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;

public record CreateVehicles(int numberOfVehicles, CompassDirection startRoad, TurnDirection turnDirection) {
}
