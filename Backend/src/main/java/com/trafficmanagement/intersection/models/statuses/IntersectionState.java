package com.trafficmanagement.intersection.models.statuses;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntersectionState {

    private final List<RoadStatus> roadStatuses = new ArrayList<>();

    public IntersectionState(Map<CompassDirection, Road> roads) {

        for (var entry : roads.entrySet()) {
            this.roadStatuses.add(new RoadStatus(entry.getKey(), entry.getValue().getRoadLineStatuses()));
        }
    }

    public List<RoadStatus> getRoadStatuses() {
        return roadStatuses;
    }
}
