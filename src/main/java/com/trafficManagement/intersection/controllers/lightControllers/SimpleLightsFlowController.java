package com.trafficManagement.intersection.controllers.lightControllers;

import com.trafficManagement.intersection.components.Road;
import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.LightColor;
import com.trafficManagement.intersection.constants.TurnDirection;
import com.trafficManagement.intersection.controllers.TrafficConfig;

import java.security.InvalidParameterException;
import java.util.EnumSet;
import java.util.Map;

public class SimpleLightsFlowController extends LightsController {

    public SimpleLightsFlowController(Map<CompassDirection, Road> roads) {
        super(roads);

        validateRoadsMap(roads);

        this.currentTurnDirection = EnumSet.of(TurnDirection.STRAIGHT);
        this.currentCompassDirections = EnumSet.of(CompassDirection.SOUTH, CompassDirection.NORTH);
    }

    private void validateRoadsMap(Map<CompassDirection, Road> roads) {
        EnumSet<CompassDirection> providedDirections = EnumSet.copyOf(roads.keySet());

        EnumSet<CompassDirection> missingDirections = EnumSet.complementOf(providedDirections);
        if(!missingDirections.isEmpty()) {
            throw new IllegalArgumentException("Roads map is missing directions: " + missingDirections);
        }
    }


    @Override
    public void makeStep() {
        stepCounter++;

        if (stepCounter > TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            if (currentTurnDirection.contains(TurnDirection.STRAIGHT)) {
                lightsSwitcher.switchLightsForDirections(currentCompassDirections, currentTurnDirection);

                currentTurnDirection = EnumSet.of(TurnDirection.LEFT);

                lightsSwitcher.switchLightsForDirections(currentCompassDirections, currentTurnDirection);
                stepCounter = 0;
            } else if (currentTurnDirection.contains(TurnDirection.LEFT)) {
                currentTurnDirection = EnumSet.of(TurnDirection.STRAIGHT);
                lightsSwitcher.switchLightsToRedForCompassDirections(currentCompassDirections);

                currentCompassDirections = getOppositeCompassDirections();
                lightsSwitcher.switchLightsForDirections(currentCompassDirections, currentTurnDirection);
                stepCounter = 0;
            }
        }
    }

    private EnumSet<CompassDirection> getOppositeCompassDirections() {
        if (currentCompassDirections.contains(CompassDirection.SOUTH) && currentCompassDirections.contains(CompassDirection.NORTH)) {
            return EnumSet.of(CompassDirection.EAST, CompassDirection.WEST);
        } else if (currentCompassDirections.contains(CompassDirection.WEST) && currentCompassDirections.contains(CompassDirection.EAST)) {
            return EnumSet.of(CompassDirection.SOUTH, CompassDirection.NORTH);
        }
        throw new InvalidParameterException("Unexpected compass directions were provided");
    }

    public LightColor getCurrentLightColor(CompassDirection compassDirection, TurnDirection turnDirection) {
        return this.lightsSwitcher.getCurrentTrafficLight(compassDirection, turnDirection);
    }
}
