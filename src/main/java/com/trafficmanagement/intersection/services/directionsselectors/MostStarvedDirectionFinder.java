package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TrafficConfig;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.trafficmanagement.intersection.helpers.CollisionDetector.doDirectionsCollide;

public class MostStarvedDirectionFinder {
    private final StarvationCounterManager starvationCounterManager;

    public MostStarvedDirectionFinder(StarvationCounterManager starvationCounterManager) {
        this.starvationCounterManager = starvationCounterManager;
    }

    public List<DirectionTurnPair> getStarvedDirectionsIfExists(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
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

    private List<Map.Entry<DirectionTurnPair, Integer>> getStarvedDirectionsListWithStarvationCount(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
        return starvationCounterManager.getStarvationCounters().entrySet().stream()
                .flatMap(directionEntry -> directionEntry.getValue().entrySet().stream()
                        .filter(turnEntry -> turnEntry.getValue() >= TrafficConfig.STARVATION_THRESHOLD)
                        .map(turnEntry -> Map.entry(new DirectionTurnPair(directionEntry.getKey(), turnEntry.getKey()), turnEntry.getValue())))
                .filter(entry -> isAnyVehicleWaiting(directionVehicleCounts, entry))
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();
    }

    private static boolean isAnyVehicleWaiting(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts, Map.Entry<DirectionTurnPair, Integer> entry) {
        return directionVehicleCounts.get(entry.getKey().compassDirection()).getOrDefault(entry.getKey().turnDirection(), 0) > 0;
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
}
