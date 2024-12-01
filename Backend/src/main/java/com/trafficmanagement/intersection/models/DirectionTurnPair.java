package com.trafficmanagement.intersection.models;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.Set;

public record DirectionTurnPair(CompassDirection compassDirection, Set<TurnDirection> turnDirections) {
}

