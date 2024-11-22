package org.app.Intersection.RoadLines;

import org.app.Intersection.Constants.TurnDirection;

import java.util.EnumSet;

public class LeftTurnRoadLine extends RoadLine {
    public LeftTurnRoadLine() {
        super(EnumSet.of(TurnDirection.LEFT));
    }
}
