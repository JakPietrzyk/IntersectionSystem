package com.trafficmanagement.intersection.controllers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.AllDirectionsRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import com.trafficmanagement.intersection.services.VehicleCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class StarvationCounterManagerTest {
    @Mock
    private VehicleCounter mockVehicleCounter;
    @Mock
    private Road mockRoad;

    private StarvationCounterManager starvationCounterManager;

    private final Set<TurnDirection> allDirections = Set.of(TurnDirection.STRAIGHT, TurnDirection.LEFT,
            TurnDirection.RIGHT);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockRoad.getAllowedDirections()).thenReturn(allDirections);
        when(mockRoad.getRoadLineLights()).thenReturn(Map.of(
                new AllDirectionsRoadLine(), new TrafficLights()
        ));

        Map<CompassDirection, Road> roads = Map.of(
                CompassDirection.NORTH, mockRoad,
                CompassDirection.SOUTH, mockRoad,
                CompassDirection.WEST, mockRoad
        );

        starvationCounterManager = new StarvationCounterManager(roads, mockVehicleCounter);
    }

    @Test
    void shouldInitializeStarvationCounters() {
        var starvationCounters = starvationCounterManager.getStarvationCounters();

        assertEquals(3, starvationCounters.size());
        for (var counter : starvationCounters.values()) {
            assertEquals(0, counter);
        }
    }

    @Test
    void shouldNotUpdateStarvationCountersWhenRoadLineEmpty() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        new DirectionTurnPair(CompassDirection.NORTH, allDirections), 0,
                        new DirectionTurnPair(CompassDirection.SOUTH, allDirections), 1
                ));

        starvationCounterManager.updateStarvationCounters(Set.of());

        var starvationCounters = starvationCounterManager.getStarvationCounters();
        assertEquals(0, starvationCounters.get(new DirectionTurnPair(CompassDirection.NORTH, allDirections)));
        assertEquals(1, starvationCounters.get(new DirectionTurnPair(CompassDirection.SOUTH, allDirections)));
    }

    @Test
    void shouldUpdateStarvationCountersWhenRoadLineNotEmpty() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        new DirectionTurnPair(CompassDirection.NORTH, allDirections), 2
                ));

        starvationCounterManager.updateStarvationCounters(Set.of());

        var starvationCounters = starvationCounterManager.getStarvationCounters();
        assertEquals(1, starvationCounters.get(new DirectionTurnPair(CompassDirection.NORTH, allDirections)));
    }

    @Test
    void shouldClearStarvationForDirection() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        new DirectionTurnPair(CompassDirection.NORTH, allDirections), 1
                ));

        starvationCounterManager.updateStarvationCounters(Set.of());
        assertEquals(1,
                starvationCounterManager.getStarvationCounters()
                        .get(new DirectionTurnPair(CompassDirection.NORTH, allDirections)));

        starvationCounterManager.clearStarvationForDirection(
                new DirectionTurnPair(CompassDirection.NORTH, allDirections));
        assertEquals(0,
                starvationCounterManager.getStarvationCounters()
                        .get(new DirectionTurnPair(CompassDirection.NORTH, allDirections)));
    }

    @Test
    void shouldAddStarvationForInactiveDirections() {
        when(mockVehicleCounter.calculateVehiclesOnEachRoadLine())
                .thenReturn(Map.of(
                        new DirectionTurnPair(CompassDirection.SOUTH, allDirections), 1,
                        new DirectionTurnPair(CompassDirection.NORTH, allDirections), 1,
                        new DirectionTurnPair(CompassDirection.WEST, allDirections), 1
                ));

        starvationCounterManager.updateStarvationCounters(
                Set.of(
                        new DirectionTurnPair(CompassDirection.SOUTH, allDirections)
                )
        );

        var starvationCounters = starvationCounterManager.getStarvationCounters();
        assertEquals(0, starvationCounters.get(new DirectionTurnPair(CompassDirection.SOUTH, allDirections)));
        assertEquals(1, starvationCounters.get(new DirectionTurnPair(CompassDirection.NORTH, allDirections)));
        assertEquals(1, starvationCounters.get(new DirectionTurnPair(CompassDirection.WEST, allDirections)));

    }
}
