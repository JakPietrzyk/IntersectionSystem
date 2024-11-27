package com.trafficManagement.intersection.components.roadLines;

import com.trafficManagement.intersection.constants.TurnDirection;

import java.util.EnumSet;

public final class AllDirectionsRoadLine extends RoadLine {
    public AllDirectionsRoadLine() {
        super(EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT, TurnDirection.LEFT));
    }
}
