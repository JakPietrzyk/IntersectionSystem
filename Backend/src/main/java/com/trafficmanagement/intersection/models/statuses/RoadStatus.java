package com.trafficmanagement.intersection.models.statuses;

import com.trafficmanagement.intersection.constants.CompassDirection;

import java.util.List;

public record RoadStatus(CompassDirection compassDirection, List<RoadLineStatus> roadLineStatus) {
}
