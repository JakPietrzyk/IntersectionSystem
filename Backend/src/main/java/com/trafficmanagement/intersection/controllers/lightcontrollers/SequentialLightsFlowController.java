package com.trafficmanagement.intersection.controllers.lightcontrollers;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TrafficConfig;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SequentialLightsFlowController extends LightsController {

    private final List<Set<DirectionTurnPair>> orderOfDirections = List.of(
            Set.of(
                    new DirectionTurnPair(CompassDirection.NORTH,
                            EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)),
                    new DirectionTurnPair(CompassDirection.SOUTH,
                            EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT))
            ),
            Set.of(
                    new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)),
                    new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT))
            ),
            Set.of(
                    new DirectionTurnPair(CompassDirection.WEST,
                            EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT)),
                    new DirectionTurnPair(CompassDirection.EAST,
                            EnumSet.of(TurnDirection.STRAIGHT, TurnDirection.RIGHT))
            ),
            Set.of(
                    new DirectionTurnPair(CompassDirection.EAST, EnumSet.of(TurnDirection.LEFT)),
                    new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.LEFT))
            )
    );

    private int directionIndex = 0;


    public SequentialLightsFlowController(Map<CompassDirection, Road> roads) {
        super(roads);
        this.currentDirections = this.orderOfDirections.get(this.directionIndex);
    }

    @Override
    public void makeStep() {
        stepCounter++;

        if (stepCounter > TrafficConfig.STEPS_BEFORE_LIGHTS_SWITCH) {
            this.directionIndex = (this.directionIndex + 1) % this.orderOfDirections.size();

            lightsSwitcher.switchLightsToRedForCompassDirections(
                    currentDirections.stream().map(DirectionTurnPair::compassDirection).collect(Collectors.toSet())
            );
            currentDirections = this.orderOfDirections.get(this.directionIndex);
            currentDirections.forEach(lightsSwitcher::switchLightsToGreenForRoadLines
            );

            stepCounter = 0;
        }
    }
}
