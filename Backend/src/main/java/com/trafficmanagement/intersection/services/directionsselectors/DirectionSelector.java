package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.helpers.CollisionDetector;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class DirectionSelector {
    private final Set<DirectionTurnPair> allRoadsLines;
    private final VehicleCounter vehicleCounter;
    private final MostStarvedDirectionFinder mostStarvedDirectionFinder;
    private final MostOverloadedDirectionFinder mostOverloadedDirectionFinder;
    private final Logger logger = LoggerFactory.getLogger(DirectionSelector.class);

    public DirectionSelector(
            Set<DirectionTurnPair> allRoadsLines,
            StarvationCounterManager starvationCounterManager,
            VehicleCounter vehicleCounter) {
        this.allRoadsLines = allRoadsLines;
        this.vehicleCounter = vehicleCounter;
        this.mostStarvedDirectionFinder = new MostStarvedDirectionFinder(starvationCounterManager);
        this.mostOverloadedDirectionFinder = new MostOverloadedDirectionFinder();
    }

    public Set<DirectionTurnPair> getDirectionsToHandle() {
        Map<DirectionTurnPair, Integer> directionVehicleCounts = vehicleCounter.calculateVehiclesOnEachRoadLine();
        Set<DirectionTurnPair> mostNeeded = mostOverloadedDirectionFinder.findMostNeededDirections(
                directionVehicleCounts);
        Set<DirectionTurnPair> starvedDirections = mostStarvedDirectionFinder.getStarvedDirectionsIfExists(
                directionVehicleCounts);

        logger.info("Found directions: mostNeeded: {} , starvedDirections: {}", mostNeeded, starvedDirections);

        var chosenDirections = !starvedDirections.isEmpty() ? starvedDirections : mostNeeded;
        return addDirectionsIfRoadLineAllowsMore(chosenDirections);
    }

    private Set<DirectionTurnPair> addDirectionsIfRoadLineAllowsMore(Set<DirectionTurnPair> newDirections) {
        for (var roadLine : allRoadsLines) {
            boolean isColliding = false;
            for (var newDirection : newDirections) {
                if (CollisionDetector.doDirectionsCollide(roadLine, newDirection)) {
                    isColliding = true;
                    break;
                }
            }
            if (!isColliding) {
                newDirections.add(roadLine);
            }
        }

        return newDirections;
    }
}
