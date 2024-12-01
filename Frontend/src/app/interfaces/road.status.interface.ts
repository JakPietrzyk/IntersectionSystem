import {RoadLineStatus} from './road.line.status.interface';
import {CompassDirection} from "../types/compassDirection.type";

export interface RoadStatus {
    compassDirection: CompassDirection;
    roadLineStatus: RoadLineStatus[];
}
