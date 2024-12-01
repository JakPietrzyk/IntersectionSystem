package com.trafficmanagement.intersection.components.intersections;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.controllers.lightcontrollers.LightsControllerWithIntensityMonitoring;

import java.util.Map;

public class SequentionalIntersection extends Intersection {
    public SequentionalIntersection(Map<CompassDirection, Road> roads) {
        super(roads, new LightsControllerWithIntensityMonitoring(roads));
    }
}