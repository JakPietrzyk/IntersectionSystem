package com.trafficmanagement;

import com.trafficmanagement.intersection.services.CommandsLineParser;
import com.trafficmanagement.intersection.services.SimulationRunner;
import com.trafficmanagement.intersection.services.SimulationSetup;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {
    private static final CommandsLineParser commandLineParser = new CommandsLineParser();
    private static final SimulationSetup simulationSetup = new SimulationSetup();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        try {
            CommandLine cmd = commandLineParser.parseArguments(args);

            if (cmd.hasOption("simulation")) {
                String intersectionType = cmd.getOptionValue("intersection");
                String inputFile = cmd.getOptionValue("input");
                String outputFile = cmd.getOptionValue("output");

                if (validateOptions(inputFile, outputFile, intersectionType)) return;

                SimulationRunner simulationRunner = simulationSetup.setupSimulation(intersectionType, inputFile,
                        outputFile);

                simulationRunner.runCommands();
                simulationRunner.saveSimulationResult();
            } else {
                SpringApplication.run(Main.class, args);
            }
        } catch (ParseException e) {
            logger.error("Parsing failed. {}", e.getMessage());
        }
    }

    private static boolean validateOptions(String inputFile, String outputFile, String intersectionType) {
        if (inputFile == null) {
            logger.info("Missing input file");
            return true;
        }
        if (outputFile == null) {
            logger.info("Missing output file");
            return true;
        }
        if (intersectionType == null) {
            logger.info("Missing intersection type");
            return true;
        }
        return false;
    }
}