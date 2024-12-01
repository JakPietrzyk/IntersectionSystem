package com.trafficmanagement.intersection.helpers;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionDetectorTests {

    @ParameterizedTest
    @MethodSource("provideTestCasesForCollision")
    void shouldDetectCollision(DirectionTurnPair pair1, DirectionTurnPair pair2, boolean expectedCollision) {
        boolean result = CollisionDetector.doDirectionsCollide(pair1, pair2);
        if (expectedCollision) {
            assertTrue(result);
        } else {
            assertFalse(result);
        }
    }

    private static Stream<Arguments> provideTestCasesForCollision() {
        return Stream.of(
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.STRAIGHT)),
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.STRAIGHT)),
                        false
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)),
                        new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.RIGHT)),
                        true
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)),
                        new DirectionTurnPair(CompassDirection.SOUTH, EnumSet.of(TurnDirection.LEFT)),
                        false
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.RIGHT)),
                        new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.LEFT)),
                        true
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.STRAIGHT)),
                        new DirectionTurnPair(CompassDirection.WEST, EnumSet.of(TurnDirection.STRAIGHT)),
                        true
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.STRAIGHT)),
                        new DirectionTurnPair(CompassDirection.NORTH, EnumSet.of(TurnDirection.LEFT)),
                        false
                )
        );
    }
}
