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
    private record DirectionTurnPair(CompassDirection compassDirection, TurnDirection turnDirection) {}

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
        List<DirectionTurnPair> selectedDirections = getDirectionsToHandle();
        switchLights(selectedDirections);
    }

    private List<DirectionTurnPair> getDirectionsToHandle() {
        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = calculateVehiclesOnEachRoadLine();
        List<DirectionTurnPair> mostNeeded = findMostNeededDirections(directionVehicleCounts);
        List<DirectionTurnPair> starvedDirections = getStarvedDirectionsIfExists(directionVehicleCounts);

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

    private List<DirectionTurnPair> findMostNeededDirections(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        List<DirectionTurnPair> allDirections = getAllDirectionsListSortedByVehicleCount(directionVehicleCounts);

        if (allDirections.isEmpty()) {
            return List.of();
        }

        List<DirectionTurnPair> result = new ArrayList<>();
        result.add(allDirections.getFirst());
        addBestNonCollidingDirection(allDirections, result);

        return result;
    }


    private static List<DirectionTurnPair> getAllDirectionsListSortedByVehicleCount(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        return directionVehicleCounts.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream()
                        .map(turnEntry -> new DirectionTurnPair(entry.getKey(), turnEntry.getKey())))
                .sorted((entry1, entry2) -> Integer.compare(
                        directionVehicleCounts.get(entry2.compassDirection).get(entry2.turnDirection),
                        directionVehicleCounts.get(entry1.compassDirection).get(entry1.turnDirection)))
                .toList();
    }

    private void addBestNonCollidingDirection(List<DirectionTurnPair> allDirections, List<DirectionTurnPair> result) {
        for (DirectionTurnPair candidate : allDirections.subList(1, allDirections.size())) {
            if (result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate))) {
                result.add(candidate);
                break;
            }
        }
    }

    private List<DirectionTurnPair> getStarvedDirectionsIfExists(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        var starvedDirections = getStarvedDirectionsListWithStarvationCount(directionVehicleCounts);

        if (starvedDirections.isEmpty()) {
            return List.of();
        }

        List<DirectionTurnPair> result = new ArrayList<>();
        result.add(starvedDirections.getFirst().getKey());

        List<DirectionTurnPair> otherStarvedDirection = getNonCollidingDirection(starvedDirections, result);
        if (!otherStarvedDirection.isEmpty()) {
            return result;
        }

        addBestNonCollidingDirection(directionVehicleCounts, result);
        return result;
    }

    private List<DirectionTurnPair> getNonCollidingDirection(List<Map.Entry<DirectionTurnPair, Integer>> starvedDirections, List<DirectionTurnPair> result) {
        for (Map.Entry<DirectionTurnPair, Integer> candidate : starvedDirections.subList(1, starvedDirections.size())) {
            if (result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate.getKey()))) {
                result.add(candidate.getKey());
                return result;
            }
        }
        return Collections.emptyList();
    }

    private void addBestNonCollidingDirection(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts, List<DirectionTurnPair> result) {
        for (var entry : directionVehicleCounts.entrySet()) {
            for (var turnEntry : entry.getValue().entrySet()) {
                DirectionTurnPair candidate = new DirectionTurnPair(entry.getKey(), turnEntry.getKey());
                if (directionVehicleCounts.get(entry.getKey()).getOrDefault(turnEntry.getKey(), 0) > 0 &&
                        result.stream().noneMatch(existing -> existing.equals(candidate) || doDirectionsCollide(existing, candidate))) {
                    result.add(candidate);
                    return;
                }
            }
        }
    }

    private List<Map.Entry<DirectionTurnPair, Integer>> getStarvedDirectionsListWithStarvationCount(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        return starvationCounters.entrySet().stream()
                .flatMap(directionEntry -> directionEntry.getValue().entrySet().stream()
                        .filter(turnEntry -> turnEntry.getValue() >= TrafficConfig.STARVATION_THRESHOLD)
                        .map(turnEntry -> Map.entry(new DirectionTurnPair(directionEntry.getKey(), turnEntry.getKey()), turnEntry.getValue())))
                .filter(entry -> isAnyVehicleWaiting(directionVehicleCounts, entry))
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();
    }

    private static boolean isAnyVehicleWaiting(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts, Map.Entry<DirectionTurnPair, Integer> entry) {
        return directionVehicleCounts.get(entry.getKey().compassDirection).getOrDefault(entry.getKey().turnDirection, 0) > 0;
    }

    private boolean doDirectionsCollide(DirectionTurnPair a,
                                        DirectionTurnPair b) {
        CompassDirection directionA = a.compassDirection;
        CompassDirection directionB = b.compassDirection;
        TurnDirection turnA = a.turnDirection;
        TurnDirection turnB = b.turnDirection;

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


    private void switchLights(List<DirectionTurnPair> newDirections) {
        lightsSwitcher.switchLightsToRedForCompassDirections(currentCompassDirections);
        updateCurrentDirections(newDirections);

        for (var directionTurnPair : newDirections) {
            clearStarvationForDirection(directionTurnPair);
            lightsSwitcher.switchLightsToGreenForDirections(directionTurnPair.compassDirection, directionTurnPair.turnDirection);
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
            CompassDirection direction = entry.compassDirection;
            TurnDirection primaryTurn = entry.turnDirection;

            EnumSet<TurnDirection> possibleTurns = roads.get(direction)
                    .getAllowedDirectionsForTurn(primaryTurn);

            currentTurnDirection.addAll(possibleTurns);
        }
    }

    private void clearStarvationForDirection(DirectionTurnPair directionTurnPair) {
        starvationCounters.get(directionTurnPair.compassDirection).put(directionTurnPair.turnDirection, 0);
    }
}

