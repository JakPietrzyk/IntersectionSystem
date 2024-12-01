package com.trafficmanagement.intersection.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.trafficmanagement.intersection.constants.AvailableCommands;

import java.io.IOException;

public class AvailableCommandsDeserializer extends JsonDeserializer<AvailableCommands> {

    @Override
    public AvailableCommands deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String direction = p.getText().toUpperCase();
        return AvailableCommands.valueOf(direction);
    }
}