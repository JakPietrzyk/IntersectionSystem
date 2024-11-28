package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StarvationCounterManagerTest {
    @Mock
    private VehicleCounter mockVehicleCounter;
    private StarvationCounterManager starvationCounterManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<CompassDirection, Road> roads = Map.of(
                CompassDirection.NORTH, mock(Road.class),
                CompassDirection.SOUTH, mock(Road.class),
                CompassDirection.WEST, mock(Road.class)
        );

        starvationCounterManager = new StarvationCounterManager(roads, mockVehicleCounter);
    }

    @Test
    void shouldInitializeStarvationCounters() {
        var starvationCounters = starvationCounterManager.getStarvationCounters();

        assertEquals(3, starvationCounters.size());
        for (Map<TurnDirection, Integer> turnCounters : starvationCounters.values()) {
            for (TurnDirection turnDirection : TurnDirection.values()) {
                assertEquals(0, turnCounters.get(turnDirection));
            }
        }
    }

    @Test
    void shouldNotUpdateStarvationCountersWhenRoadLineEmpty() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 0, TurnDirection.STRAIGHT, 1),
                        CompassDirection.SOUTH, Map.of(TurnDirection.LEFT, 1, TurnDirection.STRAIGHT, 0)
                ));

        starvationCounterManager.updateStarvationCounters(EnumSet.noneOf(CompassDirection.class), EnumSet.noneOf(TurnDirection.class));

        var starvationCounters = starvationCounterManager.getStarvationCounters();
        assertEquals(0, starvationCounters.get(CompassDirection.NORTH).get(TurnDirection.LEFT));
        assertEquals(1, starvationCounters.get(CompassDirection.NORTH).get(TurnDirection.STRAIGHT));
        assertEquals(1, starvationCounters.get(CompassDirection.SOUTH).get(TurnDirection.LEFT));
        assertEquals(0, starvationCounters.get(CompassDirection.SOUTH).get(TurnDirection.STRAIGHT));
    }

    @Test
    void shouldUpdateStarvationCountersWhenRoadLineNotEmpty() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 2)
                ));

        starvationCounterManager.updateStarvationCounters(EnumSet.noneOf(CompassDirection.class), EnumSet.noneOf(TurnDirection.class));

        var starvationCounters = starvationCounterManager.getStarvationCounters();
        assertEquals(1, starvationCounters.get(CompassDirection.NORTH).get(TurnDirection.LEFT));
    }

    @Test
    void shouldClearStarvationForDirection() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 1)
                ));

        starvationCounterManager.updateStarvationCounters(EnumSet.noneOf(CompassDirection.class), EnumSet.noneOf(TurnDirection.class));
        assertEquals(1, starvationCounterManager.getStarvationCounters().get(CompassDirection.NORTH).get(TurnDirection.LEFT));

        starvationCounterManager.clearStarvationForDirection(new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT));
        assertEquals(0, starvationCounterManager.getStarvationCounters().get(CompassDirection.NORTH).get(TurnDirection.LEFT));
    }

    @Test
    void shouldAddStarvationForInactiveDirections() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        CompassDirection.SOUTH, Map.of(TurnDirection.STRAIGHT, 1),
                        CompassDirection.NORTH, Map.of(TurnDirection.LEFT, 1),
                        CompassDirection.WEST, Map.of(TurnDirection.STRAIGHT, 1)
                ));

        starvationCounterManager.updateStarvationCounters(EnumSet.of(CompassDirection.SOUTH), EnumSet.of(TurnDirection.STRAIGHT));

        var starvationCounters = starvationCounterManager.getStarvationCounters();
        assertEquals(0, starvationCounters.get(CompassDirection.SOUTH).get(TurnDirection.STRAIGHT));
        assertEquals(1, starvationCounters.get(CompassDirection.NORTH).get(TurnDirection.LEFT));
        assertEquals(1, starvationCounters.get(CompassDirection.WEST).get(TurnDirection.STRAIGHT));

    }
}
