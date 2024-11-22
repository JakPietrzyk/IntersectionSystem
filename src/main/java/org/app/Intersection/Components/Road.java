package org.app.Intersection.Components;

import org.app.Intersection.Constants.LightColor;
import org.app.Intersection.Models.Vehicle;

public class Road {
    private final TrafficLights trafficLights;
    private final RoadLine roadLine;

    public Road() {
        this.trafficLights = new TrafficLights();
        this.roadLine = new RoadLine();
    }

    public void setGreenTrafficLight() {
        trafficLights.setGreenLight();
    }

    public boolean isGreenTrafficLight() {
        return trafficLights.isGreenLight();
    }

    public void removeVehicleFromRoadLine() {
        roadLine.removeFirstVehicle();
    }

    public void addVehicleToRoadLine(Vehicle vehicle) {
        roadLine.addVehicle(vehicle);
    }

    public int getVehicleCount() {
        return roadLine.getVehicleCount();
    }

    public LightColor getCurrentLightColor() {
        return trafficLights.getCurrentLight();
    }

    public void changeLightColor() {
        switch(trafficLights.getCurrentLight()) {
            case LightColor.RED:
                trafficLights.setGreenLight();
                break;
            case LightColor.GREEN:
                trafficLights.setRedLight();
                break;
        }
    }

    public Vehicle peekFirstVehicle() {
        return roadLine.peekFirstVehicle();
    }
}
