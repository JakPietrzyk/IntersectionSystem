package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.trafficmanagement.intersection.helpers.CollisionDetector.doDirectionsCollide;

public class MostOverloadedDirectionFinder {
    public List<DirectionTurnPair> findMostNeededDirections(Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts) {
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
                        directionVehicleCounts.get(entry2.compassDirection()).get(entry2.turnDirection()),
                        directionVehicleCounts.get(entry1.compassDirection()).get(entry1.turnDirection())))
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
}
