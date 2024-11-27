package com.trafficManagement.intersection.components;

import com.trafficManagement.intersection.components.roadLines.RoadLine;
import com.trafficManagement.intersection.constants.LightColor;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.models.Vehicle;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class Road {
    private final Map<RoadLine, TrafficLights> roadLineLights;


    public Road(Map<RoadLine, TrafficLights> roadLineLights) {
        this.roadLineLights = roadLineLights;
    }

    public void setGreenTrafficLight(TurnDirection direction) {
        var roadLines = getRoadLinesForDirection(direction);

        roadLines.forEach(roadLine -> roadLineLights.get(roadLine).setGreenLight());
    }

    private Stream<RoadLine> getRoadLinesForDirection(TurnDirection direction) {
        return roadLineLights.keySet()
                .stream()
                .filter(roadLine -> roadLine.getAllowedDirections().contains(direction));
    }

    public void setRedTrafficLightForAllRoadLines() {
        roadLineLights.values().forEach(TrafficLights::setRedLight);
    }

    private Optional<RoadLine> getFirstRoadLineForDirection(TurnDirection direction) {
        return getRoadLinesForDirection(direction)
                .findFirst();
    }

    public boolean isGreenTrafficLight(TurnDirection direction) {
        return getFirstRoadLineForDirection(direction)
                .map(roadLine -> roadLineLights.get(roadLine).isGreenLight())
                .orElse(false);
    }

    public void removeVehicleFromRoadLine(TurnDirection direction) {
        var roadLines = getRoadLinesForDirection(direction);

        roadLines.forEach(RoadLine::removeFirstVehicle);
    }

    public void addVehicleToRoadLine(Vehicle vehicle) {
        var firstRoadLine = getFirstRoadLineForDirection(vehicle.turnDirection());

        firstRoadLine.ifPresent(roadLine -> roadLine.addVehicle(vehicle));
    }

    public int getVehicleCount(TurnDirection direction) {
        return getFirstRoadLineForDirection(direction)
                .map(RoadLine::getVehicleCount)
                .orElse(0);
    }

    public LightColor getCurrentLightColor(TurnDirection direction) {
        return getFirstRoadLineForDirection(direction)
                .map(roadLine -> roadLineLights.get(roadLine).getCurrentLight())
                .orElse(LightColor.RED);
    }

    public void changeLightColor(TurnDirection direction) {
        var trafficLights = getRoadLinesForDirection(direction)
                .map(roadLineLights::get);

        trafficLights.forEach(Road::switchLightsForLights);
    }

    private static void switchLightsForLights(TrafficLights trafficLights) {
        switch(trafficLights.getCurrentLight()) {
            case LightColor.RED -> trafficLights.setGreenLight();
            case LightColor.GREEN -> trafficLights.setRedLight();
        }
    }

    public Optional<Vehicle> peekFirstVehicle(TurnDirection direction) {
        return getFirstRoadLineForDirection(direction)
                .map(RoadLine::peekFirstVehicle);
    }

    public Map<RoadLine, TrafficLights> getRoadLineLights() {
        return roadLineLights;
    }

    public EnumSet<TurnDirection> getAllowedDirectionsForTurn(TurnDirection primaryTurn) {
        return getFirstRoadLineForDirection(primaryTurn).get().getAllowedDirections();
    }
}
