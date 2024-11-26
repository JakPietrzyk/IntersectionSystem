package com.trafficManagement.intersection.components.roadLines;

import com.trafficManagement.intersection.constants.TurnDirection;

import java.util.EnumSet;

public class StraightOrRightRoadLine extends RoadLine {
    public StraightOrRightRoadLine() {
            super(EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT));
    }
}
