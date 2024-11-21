package org.app.Intersection.Models;

import org.app.Intersection.Constants.Direction;

public record Vehicle(String id, Direction startRoad, Direction endRoad) {
}
