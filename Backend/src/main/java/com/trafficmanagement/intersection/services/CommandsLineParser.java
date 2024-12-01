package com.trafficmanagement.intersection.services;

import org.apache.commons.cli.*;

public class CommandsLineParser {
    public CommandLine parseArguments(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        Option applicationType = new Option("sim", "simulation", false, "Run application type");
        Option intersectionType = new Option("it", "intersection", true, "Intersection type");
        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");

        options.addOption(applicationType);
        options.addOption(intersectionType);
        options.addOption(inputOption);
        options.addOption(outputOption);

        return parser.parse(options, args);
    }
}