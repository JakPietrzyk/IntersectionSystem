package com.trafficmanagement.intersection.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.trafficmanagement.intersection.constants.AvailableCommands;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.deserializers.AvailableCommandsDeserializer;
import com.trafficmanagement.intersection.deserializers.CompassDirectionDeserializer;

public class Command {
    @JsonDeserialize(using = AvailableCommandsDeserializer.class)
    private AvailableCommands type;
    private String vehicleId;
    @JsonDeserialize(using = CompassDirectionDeserializer.class)
    private CompassDirection startRoad;
    @JsonDeserialize(using = CompassDirectionDeserializer.class)
    private CompassDirection endRoad;

    public AvailableCommands getType() {
        return type;
    }

    public void setType(AvailableCommands type) {
        this.type = type;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public CompassDirection getStartRoad() {
        return startRoad;
    }

    public void setStartRoad(CompassDirection startRoad) {
        this.startRoad = startRoad;
    }

    public CompassDirection getEndRoad() {
        return endRoad;
    }

    public void setEndRoad(CompassDirection endRoad) {
        this.endRoad = endRoad;
    }
}