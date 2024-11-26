package com.trafficManagement.intersection.helpers;

import com.trafficManagement.intersection.constants.CompassDirection;
import com.trafficManagement.intersection.constants.TurnDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TurnDirectionPickerTests {

    @Test
    public void shouldPickLeftDirection() {
        TurnDirection pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.NORTH, CompassDirection.EAST);
        Assertions.assertEquals(TurnDirection.LEFT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.EAST, CompassDirection.SOUTH);
        Assertions.assertEquals(TurnDirection.LEFT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.SOUTH, CompassDirection.WEST);
        Assertions.assertEquals(TurnDirection.LEFT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.WEST, CompassDirection.NORTH);
        Assertions.assertEquals(TurnDirection.LEFT, pickedTurnDirection);
    }

    @Test
    public void shouldPickRightDirection() {
        TurnDirection pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.SOUTH, CompassDirection.EAST);
        Assertions.assertEquals(TurnDirection.RIGHT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.EAST, CompassDirection.NORTH);
        Assertions.assertEquals(TurnDirection.RIGHT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.NORTH, CompassDirection.WEST);
        Assertions.assertEquals(TurnDirection.RIGHT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.WEST, CompassDirection.SOUTH);
        Assertions.assertEquals(TurnDirection.RIGHT, pickedTurnDirection);
    }

    @Test
    public void shouldPickStraightDirection() {
        TurnDirection pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.SOUTH, CompassDirection.NORTH);
        Assertions.assertEquals(TurnDirection.STRAIGHT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.EAST, CompassDirection.WEST);
        Assertions.assertEquals(TurnDirection.STRAIGHT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.NORTH, CompassDirection.SOUTH);
        Assertions.assertEquals(TurnDirection.STRAIGHT, pickedTurnDirection);

        pickedTurnDirection = TurnDirectionPicker.getTurnDirection(CompassDirection.WEST, CompassDirection.EAST);
        Assertions.assertEquals(TurnDirection.STRAIGHT, pickedTurnDirection);
    }
}
