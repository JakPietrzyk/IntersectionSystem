package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.*;

import static com.trafficmanagement.intersection.helpers.CollisionDetector.doDirectionsCollide;

public class MostOverloadedDirectionFinder {
    public Set<DirectionTurnPair> findMostNeededDirections(
            Map<DirectionTurnPair, Integer> directionVehicleCounts) {
        List<DirectionTurnPair> allDirections = getAllDirectionsListSortedByVehicleCount(directionVehicleCounts);

        if (allDirections.isEmpty()) {
            return Set.of();
        }

        Set<DirectionTurnPair> result = new HashSet<>();
        result.add(allDirections.getFirst());
        addBestNonCollidingDirections(allDirections, result);

        return result;
    }


    private static List<DirectionTurnPair> getAllDirectionsListSortedByVehicleCount(
            Map<DirectionTurnPair, Integer> directionVehicleCounts) {
        return directionVehicleCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void addBestNonCollidingDirections(List<DirectionTurnPair> allDirections, Set<DirectionTurnPair> result) {
        for (DirectionTurnPair candidate : allDirections.subList(1, allDirections.size())) {
            if (result.stream().noneMatch(existing -> doDirectionsCollide(existing, candidate))) {
                result.add(candidate);
            }
        }
    }
}
