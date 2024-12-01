package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DirectionSelector {
    private final VehicleCounter vehicleCounter;
    private final MostStarvedDirectionFinder mostStarvedDirectionFinder;
    private final MostOverloadedDirectionFinder mostOverloadedDirectionFinder;
    private final Logger logger = LoggerFactory.getLogger(DirectionSelector.class);

    public DirectionSelector(StarvationCounterManager starvationCounterManager, VehicleCounter vehicleCounter) {
        this.vehicleCounter = vehicleCounter;
        this.mostStarvedDirectionFinder = new MostStarvedDirectionFinder(starvationCounterManager);
        this.mostOverloadedDirectionFinder = new MostOverloadedDirectionFinder();
    }

    public List<DirectionTurnPair> getDirectionsToHandle() {
        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = vehicleCounter.calculateVehiclesOnEachRoadLine();
        List<DirectionTurnPair> mostNeeded = mostOverloadedDirectionFinder.findMostNeededDirections(
                directionVehicleCounts);
        List<DirectionTurnPair> starvedDirections = mostStarvedDirectionFinder.getStarvedDirectionsIfExists(
                directionVehicleCounts);

        logger.info("Found directions: mostNeeded: {} , starvedDirections: {}", mostNeeded, starvedDirections);

        return !starvedDirections.isEmpty()
                ? starvedDirections
                : mostNeeded;
    }
}
