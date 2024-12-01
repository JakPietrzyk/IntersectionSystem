package com.trafficmanagement.intersection.helpers;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

public class CollisionDetector {
    public static boolean doDirectionsCollide(DirectionTurnPair a,
                                              DirectionTurnPair b) {
        CompassDirection directionA = a.compassDirection();
        CompassDirection directionB = b.compassDirection();

        for (var turnDirectionA : a.turnDirections()) {
            for (var turnDirectionB : b.turnDirections()) {
                if (doSimpleDirectionsCollide(directionA, directionB, turnDirectionA, turnDirectionB)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean doSimpleDirectionsCollide(
            CompassDirection directionA,
            CompassDirection directionB,
            TurnDirection turnA,
            TurnDirection turnB) {
        if (directionA == directionB) {
            return false;
        }

        if (directionA.isOpposite(directionB)) {
            if (turnA == TurnDirection.LEFT && turnB == TurnDirection.LEFT) {
                return false;
            }
            if (turnA == TurnDirection.LEFT || turnB == TurnDirection.LEFT) {
                return true;
            } else if (turnA == TurnDirection.STRAIGHT || turnB == TurnDirection.STRAIGHT) {
                return false;
            }
            return false;
        } else {
            return true;
        }
    }

    private CollisionDetector() {
    }
}
