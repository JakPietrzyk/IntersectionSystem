package org.app.Intersection.Controllers.LightControllers;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Controllers.TrafficLightsSwitcher;

import java.util.EnumSet;

public abstract class LightsController {
    protected final TrafficLightsSwitcher lightsSwitcher;
    protected TurnDirection currentTurnDirection;
    protected EnumSet<CompassDirection> currentCompassDirections;
    protected int stepCounter = 0;

    protected LightsController(TrafficLightsSwitcher lightsSwitcher) {
        this.lightsSwitcher = lightsSwitcher;
    }

    public abstract void makeStep();
}
