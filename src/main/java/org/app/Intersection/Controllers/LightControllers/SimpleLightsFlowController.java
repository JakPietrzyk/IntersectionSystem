package org.app.Intersection.Controllers.LightControllers;

import org.app.Intersection.Constants.CompassDirection;
import org.app.Intersection.Constants.TurnDirection;
import org.app.Intersection.Controllers.TrafficConfig;
import org.app.Intersection.Controllers.TrafficLightsSwitcher;

import java.security.InvalidParameterException;
import java.util.EnumSet;

public class SimpleLightsFlowController extends LightsController {

    public SimpleLightsFlowController(TrafficLightsSwitcher lightsSwitcher) {
        super(lightsSwitcher);
        this.currentTurnDirection = TurnDirection.STRAIGHT;
        this.currentCompassDirections = EnumSet.of(CompassDirection.SOUTH, CompassDirection.NORTH);
    }

    @Override
    public void makeStep() {
        stepCounter++;

        if(stepCounter > TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            if(currentTurnDirection == TurnDirection.STRAIGHT) {
                lightsSwitcher.switchLightsOnCompassDirectionForTurnDirection(currentCompassDirections, currentTurnDirection);

                currentTurnDirection = TurnDirection.LEFT;

                lightsSwitcher.switchLightsOnCompassDirectionForTurnDirection(currentCompassDirections , currentTurnDirection);
                stepCounter = 0;
            }
            else if(currentTurnDirection == TurnDirection.LEFT) {
                currentTurnDirection = TurnDirection.STRAIGHT;
                lightsSwitcher.switchLightsToRedForCompassDirections(currentCompassDirections);

                currentCompassDirections = getOppositeCompassDirections();
                lightsSwitcher.switchLightsOnCompassDirectionForTurnDirection(currentCompassDirections, currentTurnDirection);
                stepCounter = 0;
            }
        }
    }

    private EnumSet<CompassDirection> getOppositeCompassDirections() {
        if(currentCompassDirections.contains(CompassDirection.SOUTH) && currentCompassDirections.contains(CompassDirection.NORTH)) {
            return EnumSet.of(CompassDirection.EAST, CompassDirection.WEST);
        }
        else if(currentCompassDirections.contains(CompassDirection.WEST) && currentCompassDirections.contains(CompassDirection.EAST)) {
            return EnumSet.of(CompassDirection.SOUTH, CompassDirection.NORTH);
        }
        throw new InvalidParameterException();
    }
}
