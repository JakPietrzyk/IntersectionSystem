package com.trafficManagement.intersection.components.roadLines;

import com.trafficManagement.intersection.constants.TurnDirection;

import java.util.EnumSet;

public final class LeftTurnRoadLine extends RoadLine {
    public LeftTurnRoadLine() {
        super(EnumSet.of(TurnDirection.LEFT));
    }
}
