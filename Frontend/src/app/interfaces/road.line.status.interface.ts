import {TurnDirection} from "../types/turnDirection.type";
import {LightColor} from "../types/lightColor.type";

export interface RoadLineStatus {
  turnDirections: TurnDirection[];
  lightColor: LightColor;
  vehicleCount: number;
}
