package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.helpers.CollisionDetector;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        List<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(Map.of());

        assertEquals(List.of(), result);
    }

    @Test
    void shouldReturnTwoStarvedDirections() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(
                Map.of(
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 17),
                        CompassDirection.EAST, Map.of(TurnDirection.LEFT, 15),
                        CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 10)
                )
        );

        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 5),
                CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 3),
                CompassDirection.EAST, Map.of(TurnDirection.LEFT, 2)
        );

        try (MockedStatic<CollisionDetector> mockedCollisionDetector = mockStatic(CollisionDetector.class)) {
            mockedCollisionDetector.when(() -> CollisionDetector.doDirectionsCollide(any(), any())).thenReturn(false);

            List<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(directionVehicleCounts);

            assertEquals(2, result.size());
            assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT), result.getFirst());
            assertEquals(new DirectionTurnPair(CompassDirection.EAST, TurnDirection.LEFT), result.getLast());
        }
    }

    @Test
    void shouldReturnOneStarvedDirections() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(
                Map.of(
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 17),
                        CompassDirection.EAST, Map.of(TurnDirection.LEFT, 15),
                        CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 10)
                )
        );

        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 5),
                CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 3),
                CompassDirection.EAST, Map.of(TurnDirection.LEFT, 2)
        );

        try (MockedStatic<CollisionDetector> mockedCollisionDetector = mockStatic(CollisionDetector.class)) {
            mockedCollisionDetector.when(() -> CollisionDetector.doDirectionsCollide(any(), any())).thenReturn(true);

            List<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(directionVehicleCounts);

            assertEquals(1, result.size());
            assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT), result.getFirst());
        }
    }

    @Test
    void shouldReturnStarvedAndMostOverloadedDirectionIfOtherStarvedDirectionIsColliding() {
        when(starvationCounterManager.getStarvationCounters()).thenReturn(
                Map.of(
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 17),
                        CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 15, TurnDirection.LEFT, 1),
                        CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 10)
                )
        );

        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 5),
                CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 3, TurnDirection.LEFT, 30),
                CompassDirection.EAST, Map.of(TurnDirection.LEFT, 2)
        );


        List<DirectionTurnPair> result = finder.getStarvedDirectionsIfExists(directionVehicleCounts);

        assertEquals(2, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT), result.getFirst());
        assertEquals(new DirectionTurnPair(CompassDirection.SOUTH, TurnDirection.LEFT), result.getLast());
    }
}
