package com.trafficmanagement.intersection.components.roadlines;

import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.EnumSet;

public final class LeftTurnRoadLine extends RoadLine {
    public LeftTurnRoadLine() {
        super(EnumSet.of(TurnDirection.LEFT));
    }
}
