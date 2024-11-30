package com.trafficmanagement.intersection.models;

import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.Set;

public record RoadLineStatus(Set<TurnDirection> turnDirection, LightColor lightColor, int vehicleCount) {
}
