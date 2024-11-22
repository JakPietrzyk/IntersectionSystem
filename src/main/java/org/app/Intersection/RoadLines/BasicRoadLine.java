package org.app.Intersection.RoadLines;

import org.app.Intersection.Constants.TurnDirection;

import java.util.EnumSet;

public class BasicRoadLine extends RoadLine {
    public BasicRoadLine() {
        super(EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT, TurnDirection.LEFT));
    }
}
