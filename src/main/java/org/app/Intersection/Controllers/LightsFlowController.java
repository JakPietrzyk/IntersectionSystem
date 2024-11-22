package org.app.Intersection.Controllers;

public class LightsFlowController {
    private final TrafficLightsSwitcher lightsSwitcher;
    private final int STEPS_BEFORE_LIGHTS_SWITCH = 5;
    private int stepCounter = 0;

    public LightsFlowController(TrafficLightsSwitcher lightsSwitcher) {
        this.lightsSwitcher = lightsSwitcher;
    }

    public void makeStep() {
        stepCounter++;
        if(stepCounter > STEPS_BEFORE_LIGHTS_SWITCH) {
            lightsSwitcher.switchLights();
            stepCounter = 0;
        }
    }
}
