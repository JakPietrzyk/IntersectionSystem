import { Component, OnInit } from '@angular/core';
import {MatGridList, MatGridTile} from '@angular/material/grid-list';
import {RoadComponent} from '../road/road.component';
import {IntersectionStateInterface} from '../interfaces/intersection.state.interface'
import {IntersectionService} from '../services/intersection.service';
import {TurnDirection} from "../types/turnDirection.type";
import {CompassDirection} from "../types/compassDirection.type";
import {LightColor} from "../types/lightColor.type";

@Component({
  selector: 'app-intersection',
  templateUrl: './intersection.component.html',
  styleUrls: ['./intersection.component.css'],
  imports: [
    MatGridTile,
    RoadComponent,
    MatGridList
  ],
  standalone: true
})
export class IntersectionComponent implements OnInit {
  intersectionState: IntersectionStateInterface = { roadStatuses: [] };

  constructor(private intersectionService: IntersectionService) {}

  ngOnInit() {
    this.fetchCurrentState();
  }

  fetchCurrentState() {
    this.intersectionService.fetchCurrentState().subscribe((data: IntersectionStateInterface) => {
      this.intersectionState = data;
    });
  }

  getLaneVehicleCount(compassDirection: CompassDirection, turnDirection: TurnDirection): number {
    const road = this.intersectionState.roadStatuses.find(
      (road) => road.compassDirection === compassDirection
    );
    const lane = road?.roadLineStatus.find((lane) =>
      lane.turnDirections.includes(turnDirection)
    );
    return lane?.vehicleCount ?? 0;
  }

  getLaneLight(compassDirection: CompassDirection, turnDirection: TurnDirection): LightColor {
    const road = this.intersectionState.roadStatuses.find(
      (road) => road.compassDirection === compassDirection
    );
    const lane = road?.roadLineStatus.find((lane) =>
      lane.turnDirections.includes(turnDirection)
    );
    return lane?.lightColor ?? "RED";
  }

  stepSimulation() {
    this.intersectionService.stepSimulation().subscribe(() => this.fetchCurrentState());
  }

  restartIntersection() {
    this.intersectionService.restartIntersection().subscribe(() => this.fetchCurrentState());
  }
}
