package com.trafficmanagement.intersection.services;

import com.trafficmanagement.intersection.components.Road;
import com.trafficmanagement.intersection.components.TrafficLights;
import com.trafficmanagement.intersection.components.intersections.IntensityHandlingIntersection;
import com.trafficmanagement.intersection.components.intersections.SequentionalIntersection;
import com.trafficmanagement.intersection.components.roadlines.LeftTurnRoadLine;
import com.trafficmanagement.intersection.components.roadlines.StraightOrRightRoadLine;
import com.trafficmanagement.intersection.constants.CompassDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SimulationSetup {
    private static final Logger logger = LoggerFactory.getLogger(SimulationSetup.class);
    static Map<CompassDirection, Road> roads = Map.of(
            CompassDirection.NORTH, new Road(Map.of(
                    new LeftTurnRoadLine(), new TrafficLights(),
                    new StraightOrRightRoadLine(), new TrafficLights()
            )),
            CompassDirection.EAST, new Road(Map.of(
                    new LeftTurnRoadLine(), new TrafficLights(),
                    new StraightOrRightRoadLine(), new TrafficLights()
            )),
            CompassDirection.SOUTH, new Road(Map.of(
                    new LeftTurnRoadLine(), new TrafficLights(),
                    new StraightOrRightRoadLine(), new TrafficLights()
            )),
            CompassDirection.WEST, new Road(Map.of(
                    new LeftTurnRoadLine(), new TrafficLights(),
                    new StraightOrRightRoadLine(), new TrafficLights()
            ))
    );

    public SimulationRunner setupSimulation(String intersectionType, String inputFileName, String outputFileName) {
        SimulationRunner simulationRunner;

        if (intersectionType.equals("intensity")) {
            simulationRunner = new SimulationRunner(new IntensityHandlingIntersection(roads), inputFileName,
                    outputFileName);
            logger.info("Starting intensity intersection");
        } else {
            simulationRunner = new SimulationRunner(new SequentionalIntersection(roads), inputFileName, outputFileName);
            logger.info("Starting sequential intersection");
        }

        return simulationRunner;
    }
}