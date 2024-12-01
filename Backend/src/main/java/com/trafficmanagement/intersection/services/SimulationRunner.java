package com.trafficmanagement.intersection.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.trafficmanagement.intersection.components.intersections.Intersection;
import com.trafficmanagement.intersection.constants.AvailableCommands;
import com.trafficmanagement.intersection.models.Commands;
import com.trafficmanagement.intersection.models.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class SimulationRunner {
    private final Intersection intersection;
    private final String inputFilePath;
    private final String outputFilePath;
    private final Logger logger = LoggerFactory.getLogger(SimulationRunner.class);


    public SimulationRunner(Intersection intersection, String inputFilePath, String outputFilePath) {
        this.intersection = intersection;
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public void runCommands() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Commands commands = objectMapper.readValue(new File(inputFilePath), Commands.class);

            commands.getCommands()
                    .forEach(command -> {
                        try {
                            switch (command.getType()) {
                                case AvailableCommands.ADDVEHICLE -> intersection.addVehicle(
                                        new Vehicle(command.getVehicleId(), command.getStartRoad(),
                                                command.getEndRoad()));
                                case AvailableCommands.STEP -> intersection.step();
                            }
                        } catch (Exception e) {
                            logger.error("Error processing command: {}", e.getMessage());
                        }
                    });
        } catch (UnrecognizedPropertyException e) {
            logger.error("Parsing error: Unrecognized property in the JSON file: {}", e.getPropertyName());
        } catch (JsonMappingException e) {
            logger.error("Error mapping JSON to object: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error reading the command file", e);
        }
    }

    public void saveSimulationResult() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> result = Map.of("stepStatuses", intersection.getStepStatuses());
        String resultJson = objectMapper.writeValueAsString(result);

        logger.info("Simulation result: {}", resultJson);

        Files.writeString(
                Path.of(outputFilePath),
                resultJson,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}