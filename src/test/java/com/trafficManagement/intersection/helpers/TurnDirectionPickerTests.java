package com.trafficManagement.intersection.helpers;

import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class TurnDirectionPickerTests {
    static Stream<Arguments> provideTurnDirections() {
        return Stream.of(
                Arguments.of(CompassDirection.NORTH, CompassDirection.EAST, TurnDirection.LEFT),
                Arguments.of(CompassDirection.EAST, CompassDirection.SOUTH, TurnDirection.LEFT),
                Arguments.of(CompassDirection.SOUTH, CompassDirection.WEST, TurnDirection.LEFT),
                Arguments.of(CompassDirection.WEST, CompassDirection.NORTH, TurnDirection.LEFT),
                Arguments.of(CompassDirection.SOUTH, CompassDirection.EAST, TurnDirection.RIGHT),
                Arguments.of(CompassDirection.EAST, CompassDirection.NORTH, TurnDirection.RIGHT),
                Arguments.of(CompassDirection.NORTH, CompassDirection.WEST, TurnDirection.RIGHT),
                Arguments.of(CompassDirection.WEST, CompassDirection.SOUTH, TurnDirection.RIGHT),
                Arguments.of(CompassDirection.SOUTH, CompassDirection.NORTH, TurnDirection.STRAIGHT),
                Arguments.of(CompassDirection.EAST, CompassDirection.WEST, TurnDirection.STRAIGHT),
                Arguments.of(CompassDirection.NORTH, CompassDirection.SOUTH, TurnDirection.STRAIGHT),
                Arguments.of(CompassDirection.WEST, CompassDirection.EAST, TurnDirection.STRAIGHT)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTurnDirections")
    void shouldPickCorrectTurnDirection(CompassDirection start, CompassDirection end, TurnDirection expected) {
        TurnDirection pickedTurnDirection = TurnDirectionPicker.getTurnDirection(start, end);
        Assertions.assertEquals(expected, pickedTurnDirection);
    }
}
