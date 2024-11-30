import {RoadLineStatus} from './road.line.status.interface';

export interface RoadStatus {
  compassDirection: string;
  roadLineStatus: RoadLineStatus[];
}
