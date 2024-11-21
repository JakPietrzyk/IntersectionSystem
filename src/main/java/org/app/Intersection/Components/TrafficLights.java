package org.app.Intersection.Components;

import org.app.Intersection.Constants.LightColor;

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
