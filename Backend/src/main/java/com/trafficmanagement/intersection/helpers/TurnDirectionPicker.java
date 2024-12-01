package com.trafficmanagement.intersection.helpers;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;

import java.util.Map;

public class TurnDirectionPicker {
    static Map<CompassDirection, Integer> directionToIndex = Map.of(
            CompassDirection.NORTH, 0,
            CompassDirection.EAST, 1,
            CompassDirection.SOUTH, 2,
            CompassDirection.WEST, 3
    );

    public static TurnDirection getTurnDirection(CompassDirection startRoad, CompassDirection endRoad) {
        int startIndex = directionToIndex.get(startRoad);
        int endIndex = directionToIndex.get(endRoad);

        int difference = calculateDifference(endIndex, startIndex);

        return switch (difference) {
            case 1 -> TurnDirection.LEFT;
            case 3 -> TurnDirection.RIGHT;
            default -> TurnDirection.STRAIGHT;
        };
    }

    private static int calculateDifference(int endIndex, int startIndex) {
        return (endIndex - startIndex + 4) % 4;
    }

    public static CompassDirection getEndRoad(CompassDirection startRoad, TurnDirection turnDirection) {
        return switch (startRoad) {
            case NORTH -> switch (turnDirection) {
                case LEFT -> CompassDirection.WEST;
                case RIGHT -> CompassDirection.EAST;
                case STRAIGHT -> CompassDirection.NORTH;
            };
            case EAST -> switch (turnDirection) {
                case LEFT -> CompassDirection.NORTH;
                case RIGHT -> CompassDirection.SOUTH;
                case STRAIGHT -> CompassDirection.EAST;
            };
            case SOUTH -> switch (turnDirection) {
                case LEFT -> CompassDirection.EAST;
                case RIGHT -> CompassDirection.WEST;
                case STRAIGHT -> CompassDirection.SOUTH;
            };
            case WEST -> switch (turnDirection) {
                case LEFT -> CompassDirection.SOUTH;
                case RIGHT -> CompassDirection.NORTH;
                case STRAIGHT -> CompassDirection.WEST;
            };
        };
    }

    private TurnDirectionPicker() {
    }
}
