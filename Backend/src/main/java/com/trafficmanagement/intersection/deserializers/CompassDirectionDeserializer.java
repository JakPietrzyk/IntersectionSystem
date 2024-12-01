package com.trafficmanagement.intersection.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.trafficmanagement.intersection.constants.CompassDirection;

import java.io.IOException;

public class CompassDirectionDeserializer extends JsonDeserializer<CompassDirection> {

    @Override
    public CompassDirection deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String direction = p.getText().toUpperCase();
        return CompassDirection.valueOf(direction);
    }
}