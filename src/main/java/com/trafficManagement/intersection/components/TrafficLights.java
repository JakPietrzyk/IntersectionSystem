package com.trafficManagement.intersection.components;

import com.trafficManagement.intersection.constants.LightColor;

public class TrafficLights {
    private LightColor currentLight = LightColor.RED;

    public void setGreenLight() {
        currentLight = LightColor.GREEN;
    }

    public void setRedLight() {
        currentLight = LightColor.RED;
    }

    public boolean isGreenLight() {
        return currentLight == LightColor.GREEN;
    }

    public LightColor getCurrentLight() {
        return currentLight;
    }
}
