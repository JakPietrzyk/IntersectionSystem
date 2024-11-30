package com.trafficmanagement.intersection.models;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;

public record DirectionTurnPair(CompassDirection compassDirection, TurnDirection turnDirection) {}

