package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TrafficConfig;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;
import com.trafficmanagement.intersection.services.directionsselectors.DirectionSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LightsControllerWithIntensityMonitoring extends LightsController {

    private final Logger logger = LoggerFactory.getLogger(LightsControllerWithIntensityMonitoring.class);
    private final StarvationCounterManager starvationCounterManager;
    private final DirectionSelector directionSelector;

    public LightsControllerWithIntensityMonitoring(Map<CompassDirection, Road> roads) {
        super(roads);
        logger.info("Initializing Starvation Counters");
        VehicleCounter vehicleCounter = new VehicleCounter(roads);
        var allRoadsLines = roads.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().getRoadLineLights().keySet().stream()
                        .map(roadLine -> new DirectionTurnPair(entry.getKey(), roadLine.getAllowedDirections())))
                .collect(Collectors.toSet());
        this.starvationCounterManager = new StarvationCounterManager(roads, vehicleCounter);
        this.directionSelector = new DirectionSelector(allRoadsLines, starvationCounterManager, vehicleCounter);
    }

    @Override
    public void makeStep() {
        stepCounter++;
        starvationCounterManager.updateStarvationCounters(currentDirections);

        if (stepCounter >= TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            logger.info("Changing lights");
            logger.info("Starvation counters: {}", starvationCounterManager.getStarvationCounters());
            changeLights();
            stepCounter = 0;
        }
    }

    private void changeLights() {
        Set<DirectionTurnPair> selectedDirections = directionSelector.getDirectionsToHandle();
        switchLights(selectedDirections);
    }

    private void switchLights(Set<DirectionTurnPair> newDirections) {
        lightsSwitcher.switchLightsToRedForCompassDirections(
                currentDirections.stream().map(DirectionTurnPair::compassDirection).collect(Collectors.toSet())
        );
        currentDirections = newDirections;

        for (var directionTurnPair : newDirections) {
            starvationCounterManager.clearStarvationForDirection(directionTurnPair);
            lightsSwitcher.switchLightsToGreenForRoadLines(directionTurnPair);
        }

        logger.info("Current directions: {}", newDirections);
    }
}

