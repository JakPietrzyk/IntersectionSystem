package com.trafficmanagement.intersection.components.roadlines;

import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.EnumSet;

public final class AllDirectionsRoadLine extends RoadLine {
    public AllDirectionsRoadLine() {
        super(EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT, TurnDirection.LEFT));
    }
}
