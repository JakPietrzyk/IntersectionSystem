package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.helpers.CollisionDetector;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MostStarvedDirectionFinderTest {

    private MostStarvedDirectionFinder finder;
    private StarvationCounterManager starvationCounterManager;

    @BeforeEach
    void setUp() {
        starvationCounterManager = mock(StarvationCounterManager.class);
        finder = new MostStarvedDirectionFinder(starvationCounterManager);
    }

    @Test
    void shouldReturnEmptyListForEmptyVehicleCounts() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(Map.of());

        Set<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(Map.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTwoStarvedDirections() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(
                Map.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 17,
                        new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT)), 15,
                        new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 10
                )
        );

        Map<DirectionTurnPair, Integer> directionVehicleCounts = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 5,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 3,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT)), 2
        );

        try (MockedStatic<CollisionDetector> mockedCollisionDetector = mockStatic(CollisionDetector.class)) {
            mockedCollisionDetector.when(() -> CollisionDetector.doDirectionsCollide(any(), any())).thenReturn(false);

            Set<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(directionVehicleCounts);

            assertEquals(2, result.size());
            assertTrue(result.contains(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT))));
            assertTrue(result.contains(new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT))));
        }
    }

    @Test
    void shouldReturnOneStarvedDirections() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(
                Map.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 17,
                        new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT)), 15,
                        new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 10
                )
        );

        Map<DirectionTurnPair, Integer> directionVehicleCounts = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 5,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 3,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT)), 2
        );

        try (MockedStatic<CollisionDetector> mockedCollisionDetector = mockStatic(CollisionDetector.class)) {
            mockedCollisionDetector.when(() -> CollisionDetector.doDirectionsCollide(any(), any())).thenReturn(true);

            Set<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(directionVehicleCounts);

            assertEquals(1, result.size());
            assertEquals(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), result.iterator().next());
        }
    }

    @Test
    void shouldReturnStarvedAndMostOverloadedDirectionIfOtherStarvedDirectionIsColliding() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(
                Map.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 17,
                        new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 15,
                        new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT)), 1,
                        new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 10
                )
        );

        Map<DirectionTurnPair, Integer> directionVehicleCounts = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 5,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.STRAIGHT)), 3,
                new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT)), 30,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT)), 2
        );


        Set<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(directionVehicleCounts);

        assertEquals(2, result.size());
        assertTrue(result.contains(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT))));
        assertTrue(result.contains(new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT))));
    }
}
