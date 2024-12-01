package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MostOverloadedDirectionFinderTest {

    private MostOverloadedDirectionFinder mostOverloadedDirectionFinder;

    @BeforeEach
    void setUp() {
        mostOverloadedDirectionFinder = new MostOverloadedDirectionFinder();
    }

    @Test
    void shouldReturnEmptyListForEmptyMap() {
        Set<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(Map.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnOneNonBlockingOverloadedDirections() {
        Map<DirectionTurnPair, Integer> input = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 20,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 10,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 0,
                new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.STRAIGHT)), 19
        );

        Set<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(input);

        assertEquals(1, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), result.iterator().next());
    }

    @Test
    void shouldReturnTwoNonBlockingOverloadedDirections() {
        Map<DirectionTurnPair, Integer> input = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 20,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT)), 10,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 0,
                new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.STRAIGHT)), 19
        );

        Set<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(input);

        assertEquals(2, result.size());
        assertTrue(result.contains(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT))));
        assertTrue(result.contains(new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT))));

    }

    @Test
    void shouldReturnResultsSortedInVehicleCountOrder() {
        Map<DirectionTurnPair, Integer> input = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 10,
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.RIGHT)), 5,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 15
        );

        Set<DirectionTurnPair> result = mostOverloadedDirectionFinder.findMostNeededDirections(input);

        assertEquals(2, result.size());
        assertTrue(result.contains(new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT))));
        assertTrue(result.contains(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.RIGHT))));
    }
}
