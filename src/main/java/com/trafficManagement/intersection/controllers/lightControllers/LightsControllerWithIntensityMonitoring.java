package com.trafficManagement.intersection.controllers.lightControllers;

import com.trafficManagement.intersection.components.Road;
import com.trafficManagement.intersection.components.roadLines.RoadLine;
import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.controllers.TrafficConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class LightsControllerWithIntensityMonitoring extends LightsController {
    private final Map<CompassDirection, Map<TurnDirection, Integer>> starvationCounters = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(LightsControllerWithIntensityMonitoring.class);

    protected LightsControllerWithIntensityMonitoring(Map<CompassDirection, Road> roads) {
        super(roads);
        logger.info("Initializing Starvation Counters");

        initializeStarvationCounters(roads);
    }

    private void initializeStarvationCounters(Map<CompassDirection, Road> roads) {
        for (CompassDirection direction : roads.keySet()) {
            Map<TurnDirection, Integer> turnCounters = new EnumMap<>(TurnDirection.class);
            for (TurnDirection turn : TurnDirection.values()) {
                turnCounters.put(turn, 0);
            }
            starvationCounters.put(direction, turnCounters);
        }
    }

    @Override
    public void makeStep() {
        stepCounter++;
        updateStarvationCounters();

        if (stepCounter >= TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            logger.info("Changing lights");
            logger.info("Starvation counters: {}", starvationCounters);
            changeLights();
            stepCounter = 0;
        }
    }

    private void updateStarvationCounters() {
        for (var entry : starvationCounters.entrySet()) {
            CompassDirection compassDirection = entry.getKey();
            Map<TurnDirection, Integer> turnCounters = entry.getValue();

            for (TurnDirection turnDirection : turnCounters.keySet()) {
                updateStarvationForDirectionsIfNeeded(turnDirection, compassDirection, turnCounters);
            }
        }
    }

    private boolean isRoadLineForDirectionEmpty(TurnDirection turnDirection, CompassDirection compassDirection) {
        return calculateVehiclesOnEachRoadLine().getOrDefault(compassDirection, Map.of()).getOrDefault(turnDirection, 0) > 0;
    }

    private boolean isDirectionOrTurnInactive(TurnDirection turnDirection, CompassDirection compassDirection) {
        return !currentCompassDirections.contains(compassDirection) || !currentTurnDirection.contains(turnDirection);
    }

    private void updateStarvationForDirectionsIfNeeded(TurnDirection turnDirection, CompassDirection compassDirection, Map<TurnDirection, Integer> turnCounters) {
        if (isRoadLineForDirectionEmpty(turnDirection, compassDirection)
                && isDirectionOrTurnInactive(turnDirection, compassDirection)) {
            turnCounters.put(turnDirection, turnCounters.get(turnDirection) + 1);
        }
    }

    private void changeLights() {
        List<Map.Entry<CompassDirection, TurnDirection>> selectedDirections = getDirectionsToHandle();
        switchLights(selectedDirections);
    }

    private List<Map.Entry<CompassDirection, TurnDirection>> getDirectionsToHandle() {
        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = calculateVehiclesOnEachRoadLine();
        List<Map.Entry<CompassDirection, TurnDirection>> mostNeeded = findMostNeededDirections(directionVehicleCounts);
        List<Map.Entry<CompassDirection, TurnDirection>> starvedDirections = getStarvedDirectionsIfExists(directionVehicleCounts);

        logger.info("Found directions: mostNeeded: {} , starvedDirections: {}", mostNeeded, starvedDirections);

        return !starvedDirections.isEmpty()
                ? starvedDirections
                : mostNeeded;
    }

    private Map<CompassDirection, Map<TurnDirection, Integer>> calculateVehiclesOnEachRoadLine() {
        return roads.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<TurnDirection, Integer> turnCounts = new EnumMap<>(TurnDirection.class);
                            addAllVehiclesOnEachRoadLineForRoad(entry, turnCounts);
                            return turnCounts;
                        }
                ));
    }

    private static void addAllVehiclesOnEachRoadLineForRoad(Map.Entry<CompassDirection, Road> entry, Map<TurnDirection, Integer> turnCounts) {
        for (RoadLine line : getRoadLines(entry)) {
            for (TurnDirection direction : line.getAllowedDirections()) {
                addCountedVehicles(line, direction, turnCounts);
            }
        }
    }

    private static Set<RoadLine> getRoadLines(Map.Entry<CompassDirection, Road> entry) {
        return entry.getValue().getRoadLineLights().keySet();
    }

    private static void addCountedVehicles(RoadLine line, TurnDirection direction, Map<TurnDirection, Integer> turnCounts) {
        turnCounts.merge(direction, line.getVehicleCountForTurnDirection(direction), Integer::sum);
    }

    private List<Map.Entry<CompassDirection, TurnDirection>> findMostNeededDirections(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        List<Map.Entry<CompassDirection, TurnDirection>> allDirections = getAllDirectionsListSortedByVehicleCount(directionVehicleCounts);

        if (allDirections.isEmpty()) {
            return List.of();
        }

        List<Map.Entry<CompassDirection, TurnDirection>> result = new ArrayList<>();
        result.add(allDirections.getFirst());
        addBestNonCollidingDirection(allDirections, result);

        return result;
    }


    private static List<Map.Entry<CompassDirection, TurnDirection>> getAllDirectionsListSortedByVehicleCount(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        return directionVehicleCounts.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream()
                        .map(turnEntry -> Map.entry(entry.getKey(), turnEntry.getKey())))
                .sorted((entry1, entry2) -> Integer.compare(
                        directionVehicleCounts.get(entry2.getKey()).get(entry2.getValue()),
                        directionVehicleCounts.get(entry1.getKey()).get(entry1.getValue())))
                .toList();
    }

    private void addBestNonCollidingDirection(List<Map.Entry<CompassDirection, TurnDirection>> allDirections, List<Map.Entry<CompassDirection, TurnDirection>> result) {
        for (Map.Entry<CompassDirection, TurnDirection> candidate : allDirections.subList(1, allDirections.size())) {
            if (result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate))) {
                result.add(candidate);
                break;
            }
        }
    }

    private List<Map.Entry<CompassDirection, TurnDirection>> getStarvedDirectionsIfExists(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        var starvedDirections = getStarvedDirectionsListWithStarvationCount(directionVehicleCounts);

        if (starvedDirections.isEmpty()) {
            return List.of();
        }

        List<Map.Entry<CompassDirection, TurnDirection>> result = new ArrayList<>();
        result.add(starvedDirections.getFirst().getKey());

        List<Map.Entry<CompassDirection, TurnDirection>> otherStarvedDirection = getNonCollidingDirection(starvedDirections, result);
        if (!otherStarvedDirection.isEmpty()) {
            return result;
        }

        addBestNonCollidingDirection(directionVehicleCounts, result);
        return result;
    }

    private List<Map.Entry<CompassDirection, TurnDirection>> getNonCollidingDirection(List<Map.Entry<Map.Entry<CompassDirection, TurnDirection>, Integer>> starvedDirections, List<Map.Entry<CompassDirection, TurnDirection>> result) {
        for (Map.Entry<Map.Entry<CompassDirection, TurnDirection>, Integer> candidate : starvedDirections.subList(1, starvedDirections.size())) {
            if (result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate.getKey()))) {
                result.add(candidate.getKey());
                return result;
            }
        }
        return Collections.emptyList();
    }

    private void addBestNonCollidingDirection(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts, List<Map.Entry<CompassDirection, TurnDirection>> result) {
        for (var entry : directionVehicleCounts.entrySet()) {
            for (var turnEntry : entry.getValue().entrySet()) {
                Map.Entry<CompassDirection, TurnDirection> candidate = Map.entry(entry.getKey(), turnEntry.getKey());
                if (directionVehicleCounts.get(entry.getKey()).getOrDefault(turnEntry.getKey(), 0) > 0 &&
                        result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate) || existing.equals(candidate))) {
                    result.add(candidate);
                    return;
                }
            }
        }
    }

    private List<Map.Entry<Map.Entry<CompassDirection, TurnDirection>, Integer>> getStarvedDirectionsListWithStarvationCount(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        return starvationCounters.entrySet().stream()
                .flatMap(directionEntry -> directionEntry.getValue().entrySet().stream()
                        .filter(turnEntry -> turnEntry.getValue() >= TrafficConfig.STARVATION_THRESHOLD)
                        .map(turnEntry -> Map.entry(Map.entry(directionEntry.getKey(), turnEntry.getKey()), turnEntry.getValue())))
                .filter(entry -> isAnyVehicleWaiting(directionVehicleCounts, entry))
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();
    }

    private static boolean isAnyVehicleWaiting(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts, Map.Entry<Map.Entry<CompassDirection, TurnDirection>, Integer> entry) {
        return directionVehicleCounts.get(entry.getKey().getKey()).getOrDefault(entry.getKey().getValue(), 0) > 0;
    }

    private boolean doDirectionsCollide(Map.Entry<CompassDirection, TurnDirection> a,
                                        Map.Entry<CompassDirection, TurnDirection> b) {
        CompassDirection directionA = a.getKey();
        CompassDirection directionB = b.getKey();
        TurnDirection turnA = a.getValue();
        TurnDirection turnB = b.getValue();

        if (directionA == directionB) {
            return false;
        }

        if (directionA.isOpposite(directionB)) {
            if (turnA == TurnDirection.LEFT && turnB == TurnDirection.LEFT) {
                return false;
            }
            if (turnA == TurnDirection.LEFT || turnB == TurnDirection.LEFT) {
                return true;
            } else if (turnA == TurnDirection.STRAIGHT || turnB == TurnDirection.STRAIGHT) {
                return false;
            }
            return false;
        }

        return false;
    }


    private void switchLights(List<Map.Entry<CompassDirection, TurnDirection>> newDirections) {
        lightsSwitcher.switchLightsToRedForCompassDirections(currentCompassDirections);
        updateCurrentDirections(newDirections);

        for (var entry : newDirections) {
            clearStarvationForDirection(entry);
            lightsSwitcher.switchLightsToGreenForDirections(entry.getKey(), entry.getValue());
        }

        logger.info("CurrentCompassDirections: {}, CurrentTurnDirections: {}", currentCompassDirections, currentTurnDirection);
    }

    private void updateCurrentDirections(List<Map.Entry<CompassDirection, TurnDirection>> newDirections) {
        currentCompassDirections = newDirections.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(CompassDirection.class)));
        currentTurnDirection = newDirections.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(TurnDirection.class)));

        addDirectionsIfRoadLineAllowsMore(newDirections);
    }

    private void addDirectionsIfRoadLineAllowsMore(List<Map.Entry<CompassDirection, TurnDirection>> newDirections) {
        for (var entry : newDirections) {
            CompassDirection direction = entry.getKey();
            TurnDirection primaryTurn = entry.getValue();

            EnumSet<TurnDirection> possibleTurns = roads.get(direction)
                    .getAllowedDirectionsForTurn(primaryTurn);

            currentTurnDirection.addAll(possibleTurns);
        }
    }

    private void clearStarvationForDirection(Map.Entry<CompassDirection, TurnDirection> entry) {
        starvationCounters.get(entry.getKey()).put(entry.getValue(), 0);
    }
}

