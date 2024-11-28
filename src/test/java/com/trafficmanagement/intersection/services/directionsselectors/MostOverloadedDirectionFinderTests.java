package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MostOverloadedDirectionFinderTest {

    private MostOverloadedDirectionFinder mostOverloadedDirectionFinder;

    @BeforeEach
    void setUp() {
        mostOverloadedDirectionFinder = new MostOverloadedDirectionFinder();
    }

    @Test
    void shouldReturnEmptyListForEmptyMap() {
        List<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(Map.of());

        assertEquals(List.of(), result);
    }

    @Test
    void shouldReturnOneNonBlockingOverloadedDirections() {
        Map<CompassDirection, Map<TurnDirection, Integer>> input = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 20),
                CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 10),
                CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 0),
                CompassDirection.WEST, Map.of(TurnDirection.STRAIGHT, 19)
        );

        List<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(input);

        assertEquals(1, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT), result.getFirst());
    }

    @Test
    void shouldReturnTwoNonBlockingOverloadedDirections() {
        Map<CompassDirection, Map<TurnDirection, Integer>> input = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 20),
                CompassDirection.SOUTH, Map.of(TurnDirection.LEFT, 10),
                CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 0),
                CompassDirection.WEST, Map.of(TurnDirection.STRAIGHT, 19)
        );

        List<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(input);

        assertEquals(2, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT), result.getFirst());
        assertEquals(new DirectionTurnPair(CompassDirection.SOUTH, TurnDirection.LEFT), result.getLast());

    }

    @Test
    void shouldReturnResultsSortedInVehicleCountOrder() {
        Map<CompassDirection, Map<TurnDirection, Integer>> input = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 10, TurnDirection.RIGHT, 5),
                CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 15)
        );

        List<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(input);

        assertEquals(2, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.SOUTH, TurnDirection.STRAIGHT), result.get(0));
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.RIGHT), result.get(1));
    }
}
