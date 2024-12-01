package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.TrafficConfig;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.*;

import static com.trafficmanagement.intersection.helpers.CollisionDetector.doDirectionsCollide;

public class MostStarvedDirectionFinder {
    private final StarvationCounterManager starvationCounterManager;

    public MostStarvedDirectionFinder(StarvationCounterManager starvationCounterManager) {
        this.starvationCounterManager = starvationCounterManager;
    }

    public Set<DirectionTurnPair> getStarvedDirectionsIfExists(
            Map<DirectionTurnPair, Integer> directionVehicleCounts) {
        var starvedDirections = getStarvedDirectionsListWithStarvationCount(directionVehicleCounts);

        if (starvedDirections.isEmpty()) {
            return Set.of();
        }

        Set<DirectionTurnPair> result = new HashSet<>();
        result.add(starvedDirections.getFirst().getKey());

        Set<DirectionTurnPair> otherStarvedDirection = addNonCollidingDirection(starvedDirections, result);
        if (!otherStarvedDirection.isEmpty()) {
            return result;
        }

        addBestNonCollidingDirections(directionVehicleCounts, result);
        return result;
    }

    private List<Map.Entry<DirectionTurnPair, Integer>> getStarvedDirectionsListWithStarvationCount(
            Map<DirectionTurnPair, Integer> directionVehicleCounts) {

        return starvationCounterManager.getStarvationCounters().entrySet().stream()
                .filter(directionEntry -> directionEntry.getValue() >= TrafficConfig.STARVATION_THRESHOLD)
                .filter(entry -> isAnyVehicleWaiting(directionVehicleCounts, entry.getKey()))
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();
    }

    private static boolean isAnyVehicleWaiting(
            Map<DirectionTurnPair, Integer> directionVehicleCounts,
            DirectionTurnPair directionTurnPair) {
        return directionVehicleCounts.getOrDefault(directionTurnPair, 0) > 0;
    }

    private Set<DirectionTurnPair> addNonCollidingDirection(
            List<Map.Entry<DirectionTurnPair, Integer>> starvedDirections, Set<DirectionTurnPair> result) {
        for (Map.Entry<DirectionTurnPair, Integer> candidate : starvedDirections.subList(1, starvedDirections.size())) {
            if (result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate.getKey()))) {
                result.add(candidate.getKey());
                return result;
            }
        }
        return Collections.emptySet();
    }

    private void addBestNonCollidingDirections(Map<DirectionTurnPair, Integer> directionVehicleCounts,
                                               Set<DirectionTurnPair> result) {
        for (var entry : directionVehicleCounts.entrySet()) {
            DirectionTurnPair candidate = entry.getKey();
            if (directionVehicleCounts.getOrDefault(candidate, 0) > 0 &&
                    result.stream().noneMatch(existing -> existing.equals(candidate) || doDirectionsCollide(existing, candidate))) {
                result.add(candidate);
            }
        }
    }
}
