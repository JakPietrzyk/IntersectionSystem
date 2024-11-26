package org.app.Intersection.Controllers.LightControllers;

import org.app.Intersection.Components.Road;
import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Controllers.TrafficLightsSwitcher;

import java.util.EnumSet;
import java.util.Map;

public abstract class LightsController {
    protected final TrafficLightsSwitcher lightsSwitcher;
    protected EnumSet<TurnDirection> currentTurnDirection;
    protected EnumSet<CompassDirection> currentCompassDirections;
    protected int stepCounter = 0;
    protected final Map<CompassDirection, Road> roads;

    protected LightsController(Map<CompassDirection, Road> roads) {
        this.roads = roads;
        this.lightsSwitcher = new TrafficLightsSwitcher(roads);
        currentCompassDirections = EnumSet.of(CompassDirection.NORTH, CompassDirection.SOUTH);
        currentTurnDirection = EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT);
    }

    public abstract void makeStep();
}
