package com.trafficmanagement.intersection.models.statuses;

import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.Set;

public record RoadLineStatus(Set<TurnDirection> turnDirections, LightColor lightColor, int vehicleCount) {
}
