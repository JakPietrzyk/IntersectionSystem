package org.app.Intersection.RoadLines;

import org.app.Intersection.Constants.TurnDirection;

import java.util.EnumSet;

public class StraightOrRightRoadLine extends RoadLine {
    public StraightOrRightRoadLine() {
            super(EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT));
    }
}
