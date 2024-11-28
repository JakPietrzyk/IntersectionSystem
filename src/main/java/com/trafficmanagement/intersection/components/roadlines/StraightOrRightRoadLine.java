package com.trafficmanagement.intersection.components.roadlines;

import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.EnumSet;

public final class StraightOrRightRoadLine extends RoadLine {
    public StraightOrRightRoadLine() {
        super(EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT));
    }
}
