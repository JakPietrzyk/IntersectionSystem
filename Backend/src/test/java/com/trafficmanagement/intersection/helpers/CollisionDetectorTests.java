package com.trafficmanagement.intersection.helpers;

import com.trafficmanagement.intersection.constants.CompassDirection;
import com.trafficmanagement.intersection.constants.TurnDirection;
import com.trafficmanagement.intersection.models.DirectionTurnPair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

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
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.STRAIGHT),
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.STRAIGHT),
                        false
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT),
                        new DirectionTurnPair(CompassDirection.SOUTH, TurnDirection.RIGHT),
                        true
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT),
                        new DirectionTurnPair(CompassDirection.SOUTH, TurnDirection.LEFT),
                        false
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.RIGHT),
                        new DirectionTurnPair(CompassDirection.WEST, TurnDirection.LEFT),
                        true
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.STRAIGHT),
                        new DirectionTurnPair(CompassDirection.WEST, TurnDirection.STRAIGHT),
                        true
                ),
                Arguments.of(
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.STRAIGHT),
                        new DirectionTurnPair(CompassDirection.NORTH, TurnDirection.LEFT),
                        false
                )
        );
    }
}
