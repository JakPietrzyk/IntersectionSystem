package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TrafficConfig;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.*;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.directionsselectors.DirectionSelector;
import com.trafficmanagement.intersection.services.VehicleCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class LightsControllerWithIntensityMonitoring extends LightsController {

    private final Logger logger = LoggerFactory.getLogger(LightsControllerWithIntensityMonitoring.class);
    private final StarvationCounterManager starvationCounterManager;
    private final DirectionSelector directionSelector;

    protected LightsControllerWithIntensityMonitoring(Map<CompassDirection, Road> roads) {
        super(roads);
        logger.info("Initializing Starvation Counters");
        VehicleCounter vehicleCounter = new VehicleCounter(roads);

        this.starvationCounterManager = new StarvationCounterManager(roads, vehicleCounter);
        this.directionSelector = new DirectionSelector(starvationCounterManager, vehicleCounter);
    }

    @Override
    public void makeStep() {
        stepCounter++;
        starvationCounterManager.updateStarvationCounters(currentCompassDirections, currentTurnDirection);

        if (stepCounter >= TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            logger.info("Changing lights");
            logger.info("Starvation counters: {}", starvationCounterManager.getStarvationCounters());
            changeLights();
            stepCounter = 0;
        }
    }

    private void changeLights() {
        List<DirectionTurnPair> selectedDirections = directionSelector.getDirectionsToHandle();
        switchLights(selectedDirections);
    }

    private void switchLights(List<DirectionTurnPair> newDirections) {
        lightsSwitcher.switchLightsToRedForCompassDirections(currentCompassDirections);
        updateCurrentDirections(newDirections);

        for (var directionTurnPair : newDirections) {
            starvationCounterManager.clearStarvationForDirection(directionTurnPair);
            lightsSwitcher.switchLightsToGreenForDirections(directionTurnPair.compassDirection(), directionTurnPair.turnDirection());
        }

        logger.info("CurrentCompassDirections: {}, CurrentTurnDirections: {}", currentCompassDirections, currentTurnDirection);
    }

    private void updateCurrentDirections(List<DirectionTurnPair> newDirections) {
        currentCompassDirections = newDirections.stream()
                .map(DirectionTurnPair::compassDirection)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(CompassDirection.class)));
        currentTurnDirection = newDirections.stream()
                .map(DirectionTurnPair::turnDirection)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(TurnDirection.class)));

        addDirectionsIfRoadLineAllowsMore(newDirections);
    }

    private void addDirectionsIfRoadLineAllowsMore(List<DirectionTurnPair> newDirections) {
        for (var entry : newDirections) {
            CompassDirection direction = entry.compassDirection();
            TurnDirection primaryTurn = entry.turnDirection();

            Set<TurnDirection> possibleTurns = roads.get(direction)
                    .getAllowedDirectionsForTurn(primaryTurn);

            currentTurnDirection.addAll(possibleTurns);
        }
    }
}

