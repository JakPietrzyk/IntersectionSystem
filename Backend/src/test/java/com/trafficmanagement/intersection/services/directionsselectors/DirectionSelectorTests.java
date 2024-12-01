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

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DirectionSelectorTest {

    private DirectionSelector directionSelector;
    @Mock
    private VehicleCounter vehicleCounter;
    @Mock
    private StarvationCounterManager starvationCounterManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        directionSelector = new DirectionSelector(Set.of(), starvationCounterManager, vehicleCounter);
    }

    @AfterEach
    void tearDown() {
        Mockito.clearAllCaches();
    }

    @Test
    void shouldReturnStarvedDirections() {
        Map<DirectionTurnPair, Integer> directionVehicleCounts = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 5,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 3
        );

        when(starvationCounterManager.getStarvationCounters()).thenReturn(Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 15,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 3
        ));

        when(vehicleCounter.calculateVehiclesOnEachRoadLine()).thenReturn(directionVehicleCounts);

        Set<DirectionTurnPair> result = directionSelector.getDirectionsToHandle();

        assertEquals(1, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)),
                result.iterator().next());
    }

    @Test
    void shouldReturnMostNeededDirection() {
        Map<DirectionTurnPair, Integer> directionVehicleCounts = Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 5,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 3,
                new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.LEFT)), 15
        );

        when(starvationCounterManager.getStarvationCounters()).thenReturn(Map.of(
                new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)), 4,
                new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.STRAIGHT)), 3,
                new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.LEFT)), 3
        ));

        when(vehicleCounter.calculateVehiclesOnEachRoadLine()).thenReturn(directionVehicleCounts);

        Set<DirectionTurnPair> result = directionSelector.getDirectionsToHandle();

        assertEquals(1, result.size());
        assertEquals(new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.LEFT)),
                result.iterator().next());
    }
}
