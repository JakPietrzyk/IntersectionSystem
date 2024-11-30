package com.trafficmanagement.intersection.services.directionsselectors;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.controllers.StarvationCounterManager;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DirectionSelectorTest {

    private DirectionSelector directionSelector;
    @Mock
    private VehicleCounter vehicleCounter;
    @Mock
    private StarvationCounterManager starvationCounterManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        directionSelector = new DirectionSelector(starvationCounterManager, vehicleCounter);
    }

    @AfterEach
    void tearDown() {
        Mockito.clearAllCaches();
    }

    @Test
    void shouldReturnStarvedDirections() {
        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 5),
                CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 3)
        );

        when(starvationCounterManager.getStarvationCounters()).thenReturn(Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 15),
                CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 3)
        ));

        when(vehicleCounter.calculateVehiclesOnEachRoadLine()).thenReturn(directionVehicleCounts);

        List<DirectionTurnPair> result = directionSelector.getDirectionsToHandle();

        assertEquals(1, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT), result.getFirst());
    }

    @Test
    void shouldReturnMostNeededDirection() {
        Map<CompassDirection, Map<TurnDirection, Integer>> directionVehicleCounts = Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 5),
                CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 3),
                CompassDirection.WEST, Map.of(TurnDirection.LEFT, 15)
        );

        when(starvationCounterManager.getStarvationCounters()).thenReturn(Map.of(
                CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 4),
                CompassDirection.EAST, Map.of(TurnDirection.STRAIGHT, 3),
                CompassDirection.WEST, Map.of(TurnDirection.LEFT, 3)
        ));

        when(vehicleCounter.calculateVehiclesOnEachRoadLine()).thenReturn(directionVehicleCounts);

        List<DirectionTurnPair> result = directionSelector.getDirectionsToHandle();

        assertEquals(1, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.WEST, TurnDirection.LEFT), result.getFirst());
    }
}
