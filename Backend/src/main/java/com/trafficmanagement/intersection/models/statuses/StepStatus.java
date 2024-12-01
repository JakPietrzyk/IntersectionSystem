package com.trafficmanagement.intersection.models.statuses;

import java.util.List;

public class StepStatus {
    private final List<String> leftVehicles;

    public StepStatus(List<String> leftVehicles) {
        this.leftVehicles = leftVehicles;
    }

    public List<String> getLeftVehicles() {
        return leftVehicles;
    }
}