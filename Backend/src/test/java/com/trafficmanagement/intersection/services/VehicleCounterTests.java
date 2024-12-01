package com.trafficmanagement.intersection.services;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.RoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.EnumSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class VehicleCounterTest {

    @Mock
    private Road roadNorth;
    @Mock
    private RoadLine northRoadLineStraightRight;
    @Mock
    private RoadLine northRoadLineLeft;

    @Mock
    private Road roadSouth;
    @Mock
    private RoadLine southRoadLineAllDirections;
    @Mock
    private TrafficLights trafficLights;

    private VehicleCounter vehicleCounter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<CompassDirection, Road> roads = Map.of(
                CompassDirection.NORTH, roadNorth,
                CompassDirection.SOUTH, roadSouth
        );

        vehicleCounter = new VehicleCounter(roads);

        mockNorthRoad();
        mockSouthRoad();
    }

    private void mockNorthRoad() {
        when(roadNorth.getRoadLineLights()).thenReturn(Map.of(
                northRoadLineStraightRight, trafficLights,
                northRoadLineLeft, trafficLights
        ));

        when(northRoadLineStraightRight.getAllowedDirections()).thenReturn(
                EnumSet.of(TurnDirection.RIGHT, TurnDirection.STRAIGHT));
        when(northRoadLineStraightRight.getVehicleCountForTurnDirection(TurnDirection.RIGHT)).thenReturn(3);
        when(northRoadLineStraightRight.getVehicleCountForTurnDirection(TurnDirection.STRAIGHT)).thenReturn(2);

        when(northRoadLineLeft.getAllowedDirections()).thenReturn(EnumSet.of(TurnDirection.LEFT));
        when(northRoadLineLeft.getVehicleCountForTurnDirection(TurnDirection.LEFT)).thenReturn(4);
    }

    private void mockSouthRoad() {
        when(roadSouth.getRoadLineLights()).thenReturn(Map.of(
                southRoadLineAllDirections, trafficLights
        ));

        when(southRoadLineAllDirections.getAllowedDirections()).thenReturn(
                EnumSet.of(TurnDirection.LEFT, TurnDirection.STRAIGHT, TurnDirection.RIGHT));
        when(southRoadLineAllDirections.getVehicleCountForTurnDirection(TurnDirection.LEFT)).thenReturn(5);
        when(southRoadLineAllDirections.getVehicleCountForTurnDirection(TurnDirection.STRAIGHT)).thenReturn(1);
        when(southRoadLineAllDirections.getVehicleCountForTurnDirection(TurnDirection.RIGHT)).thenReturn(2);
    }

    @Test
    void shouldCalculateCorrectly() {
        Map<CompassDirection, Map<TurnDirection, Integer>> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(2, result.size());

        Map<TurnDirection, Integer> northCounts = result.get(CompassDirection.NORTH);
        assertEquals(3, northCounts.get(TurnDirection.RIGHT));
        assertEquals(2, northCounts.get(TurnDirection.STRAIGHT));
        assertEquals(4, northCounts.get(TurnDirection.LEFT));

        Map<TurnDirection, Integer> southCounts = result.get(CompassDirection.SOUTH);
        assertEquals(5, southCounts.get(TurnDirection.LEFT));
        assertEquals(1, southCounts.get(TurnDirection.STRAIGHT));
        assertEquals(2, southCounts.get(TurnDirection.RIGHT));
    }

    @Test
    void shouldCalculateCorrectlyIfOneRoadIsEmpty() {
        when(roadNorth.getRoadLineLights()).thenReturn(Map.of());

        Map<CompassDirection, Map<TurnDirection, Integer>> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(2, result.size());
        assertEquals(0, result.get(CompassDirection.NORTH).size());
        assertEquals(3, result.get(CompassDirection.SOUTH).size());
    }

    @Test
    void shouldReturnEmptyResultsForEachRoadIfNoRoads() {
        when(roadNorth.getRoadLineLights()).thenReturn(Map.of());
        when(roadSouth.getRoadLineLights()).thenReturn(Map.of());

        Map<CompassDirection, Map<TurnDirection, Integer>> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(2, result.size());
        assertEquals(0, result.get(CompassDirection.NORTH).size());
        assertEquals(0, result.get(CompassDirection.SOUTH).size());
    }

    @Test
    void shouldReturnEmptyResultIfNoRoads() {
        vehicleCounter = new VehicleCounter(Map.of());

        Map<CompassDirection, Map<TurnDirection, Integer>> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(0, result.size());
    }
}
