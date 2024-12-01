import {CompassDirection} from "../types/compassDirection.type";
import {TurnDirection} from "../types/turnDirection.type";

export interface CreateVehicles {
    numberOfVehicles: number;
    startRoad: CompassDirection;
    turnDirection: TurnDirection;
}