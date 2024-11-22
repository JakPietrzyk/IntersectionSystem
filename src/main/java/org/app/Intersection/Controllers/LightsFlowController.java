package org.app.Intersection.Controllers;

import org.app.Intersection.Constants.TurnDirection;

public class LightsFlowController {
    private final TrafficLightsSwitcher lightsSwitcher;
    private int stepCounter = 0;

    public LightsFlowController(TrafficLightsSwitcher lightsSwitcher) {
        this.lightsSwitcher = lightsSwitcher;
    }

    public void makeStep() {
        stepCounter++;
        if(stepCounter > TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            lightsSwitcher.switchLights(TurnDirection.STRAIGHT);
            stepCounter = 0;
        }
    }
}
