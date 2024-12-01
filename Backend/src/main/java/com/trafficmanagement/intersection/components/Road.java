package com.trafficmanagement.intersection.components;

import com.trafficmanagement.intersection.components.roadlines.RoadLine;
import com.trafficmanagement.intersection.constants.LightColor;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.Vehicle;
import com.trafficmanagement.intersection.models.statuses.RoadLineStatus;

import java.util.*;
import java.util.stream.Collectors;
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

    public void setGreenTrafficLight(Set<TurnDirection> directions) {
        var roadLines = getRoadLinesForDirections(directions).collect(Collectors.toSet());
        roadLines.forEach(roadLine -> roadLineLights.get(roadLine).setGreenLight());
    }

    private Stream<RoadLine> getRoadLinesForDirection(TurnDirection direction) {
        return roadLineLights.keySet()
                .stream()
                .filter(roadLine -> roadLine.getAllowedDirections().contains(direction));
    }

    private Stream<RoadLine> getRoadLinesForDirections(Set<TurnDirection> directions) {
        return roadLineLights.keySet()
                .stream()
                .filter(roadLine -> {
                    var allowedDirections = new HashSet<>(roadLine.getAllowedDirections());
                    allowedDirections.removeAll(directions);
                    return allowedDirections.isEmpty();
                });
    }

    public void setRedTrafficLightForAllRoadLines() {
        roadLineLights.values().forEach(TrafficLights::setRedLight);
    }

    private Optional<RoadLine> getFirstRoadLineForDirection(TurnDirection direction) {
        return getRoadLinesForDirection(direction)
                .findFirst();
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

    public LightColor getCurrentLightColor(Set<TurnDirection> directions) {
        return getRoadLinesForDirections(directions)
                .findAny()
                .map(roadLine -> roadLineLights.get(roadLine).getCurrentLight())
                .orElse(LightColor.RED);
    }

    public Map<RoadLine, TrafficLights> getRoadLineLights() {
        return roadLineLights;
    }

    public List<RoadLineStatus> getRoadLineStatuses() {
        return this.roadLineLights.entrySet().stream()
                .map(roadLineLight -> new RoadLineStatus(
                        roadLineLight.getKey().getAllowedDirections(),
                        roadLineLight.getValue().getCurrentLight(),
                        roadLineLight.getKey().getVehicleCount()))
                .toList();
    }

    public Set<TurnDirection> getAllowedDirections() {
        return roadLineLights.keySet().stream()
                .flatMap(roadLine -> roadLine.getAllowedDirections().stream())
                .collect(Collectors.toSet());
    }
}
