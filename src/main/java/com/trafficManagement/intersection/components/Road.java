package com.trafficManagement.intersection.components;

import com.trafficManagement.intersection.constants.LightColor;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.models.Vehicle;
import com.trafficManagement.intersection.components.roadLines.RoadLine;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

public class Road {
    private final Map<RoadLine, TrafficLights> roadLineLights;


    public Road(Map<RoadLine, TrafficLights> roadLineLights) {
        this.roadLineLights = roadLineLights;
    }

    public void setGreenTrafficLight(TurnDirection direction) {
        var firstRoadLine = getFirstRoadLineForDirection(direction);

        firstRoadLine.ifPresent(roadLine -> roadLineLights.get(roadLine).setGreenLight());
    }

    public void setRedTrafficLightForAllRoadLines() {
        roadLineLights.values().forEach(TrafficLights::setRedLight);
    }

    private Optional<RoadLine> getFirstRoadLineForDirection(TurnDirection direction) {
        return roadLineLights.keySet()
                .stream()
                .filter(roadLine -> roadLine.getAllowedDirections().contains(direction))
                .findFirst();
    }

    public boolean isGreenTrafficLight(TurnDirection direction) {
        return getFirstRoadLineForDirection(direction)
                .map(roadLine -> roadLineLights.get(roadLine).isGreenLight())
                .orElse(false);
    }

    public void removeVehicleFromRoadLine(TurnDirection direction) {
        var firstRoadLine = getFirstRoadLineForDirection(direction);

        firstRoadLine.ifPresent(RoadLine::removeFirstVehicle);
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
        var trafficLights = getFirstRoadLineForDirection(direction)
                .map(roadLineLights::get)
                .orElseThrow();

        switch(trafficLights.getCurrentLight()) {
            case LightColor.RED:
                trafficLights.setGreenLight();
                break;
            case LightColor.GREEN:
                trafficLights.setRedLight();
                break;
        }
    }

    public Vehicle peekFirstVehicle(TurnDirection direction) {
        return getFirstRoadLineForDirection(direction)
                .map(RoadLine::peekFirstVehicle)
                .orElseThrow();
    }

    public Map<RoadLine, TrafficLights> getRoadLineLights() {
        return roadLineLights;
    }

    public EnumSet<TurnDirection> getAllowedDirectionsForTurn(TurnDirection primaryTurn) {
        return getFirstRoadLineForDirection(primaryTurn).get().getAllowedDirections();
    }
}
