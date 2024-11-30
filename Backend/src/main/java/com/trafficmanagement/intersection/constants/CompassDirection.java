package com.trafficmanagement.intersection.constants;

public enum CompassDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public boolean isOpposite(CompassDirection otherDirection) {
        return switch (this) {
            case NORTH -> otherDirection == CompassDirection.SOUTH;
            case SOUTH -> otherDirection == CompassDirection.NORTH;
            case EAST -> otherDirection == CompassDirection.WEST;
            case WEST -> otherDirection == CompassDirection.EAST;
        };
    }
}
