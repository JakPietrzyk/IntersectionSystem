package com.trafficmanagement.intersection.services;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.roadlines.RoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
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

        when(northRoadLineStraightRight.getVehicleCount()).thenReturn(5);
        when(northRoadLineLeft.getVehicleCount()).thenReturn(4);

        when(northRoadLineStraightRight.getAllowedDirections()).thenReturn(
                EnumSet.of(TurnDirection.RIGHT, TurnDirection.STRAIGHT));
        when(northRoadLineLeft.getAllowedDirections()).thenReturn(EnumSet.of(TurnDirection.LEFT));
    }

    private void mockSouthRoad() {
        when(roadSouth.getRoadLineLights()).thenReturn(Map.of(
                southRoadLineAllDirections, trafficLights
        ));

        when(southRoadLineAllDirections.getVehicleCount()).thenReturn(8);
        when(southRoadLineAllDirections.getAllowedDirections()).thenReturn(
                EnumSet.of(TurnDirection.LEFT, TurnDirection.STRAIGHT, TurnDirection.RIGHT));
    }

    @Test
    void shouldCalculateCorrectly() {
        Map<DirectionTurnPair, Integer> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(3, result.size());

        assertEquals(5, result.get(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.RIGHT, TurnDirection.STRAIGHT))));
        assertEquals(4, result.get(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT))));
        assertEquals(8, result.get(new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT, TurnDirection.STRAIGHT, TurnDirection.RIGHT))));
    }

    @Test
    void shouldCalculateCorrectlyIfOneRoadIsEmpty() {
        when(northRoadLineStraightRight.getVehicleCount()).thenReturn(0);
        when(northRoadLineLeft.getVehicleCount()).thenReturn(0);

        Map<DirectionTurnPair, Integer> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(3, result.size());
        assertEquals(0, result.get(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.RIGHT, TurnDirection.STRAIGHT))));
        assertEquals(0, result.get(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT))));
        assertEquals(8, result.get(new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT, TurnDirection.STRAIGHT, TurnDirection.RIGHT))));
    }

    @Test
    void shouldReturnEmptyResultsForEachRoadIfNoRoads() {
        when(northRoadLineStraightRight.getVehicleCount()).thenReturn(0);
        when(northRoadLineLeft.getVehicleCount()).thenReturn(0);
        when(southRoadLineAllDirections.getVehicleCount()).thenReturn(0);

        Map<DirectionTurnPair, Integer> result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(3, result.size());
        assertEquals(0, result.get(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.RIGHT, TurnDirection.STRAIGHT))));
        assertEquals(0, result.get(new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT))));
        assertEquals(0, result.get(new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT, TurnDirection.STRAIGHT, TurnDirection.RIGHT))));

    }

    @Test
    void shouldReturnEmptyResultIfNoRoads() {
        vehicleCounter = new VehicleCounter(Map.of());

        var result = vehicleCounter.calculateVehiclesOnEachRoadLine();

        assertEquals(0, result.size());
    }
}
