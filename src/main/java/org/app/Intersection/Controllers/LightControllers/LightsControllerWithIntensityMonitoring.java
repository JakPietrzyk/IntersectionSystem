package org.app.Intersection.Controllers.LightControllers;

import org.app.Intersection.Controllers.TrafficLightsSwitcher;

public class LightsControllerWithIntensityMonitoring extends LightsController {


    protected LightsControllerWithIntensityMonitoring(TrafficLightsSwitcher lightsSwitcher) {
        super(lightsSwitcher);
    }

    @Override
    public void makeStep() {

    }
}

