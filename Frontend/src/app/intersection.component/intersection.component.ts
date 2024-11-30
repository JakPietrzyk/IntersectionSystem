import { Component, OnInit } from '@angular/core';
import {MatGridList, MatGridTile} from '@angular/material/grid-list';
import {RoadComponent} from '../road.component/road.component';
import {IntersectionStateInterface} from '../interfaces/intersection.state.interface'
import {IntersectionService} from '../services/intersection.service';

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

  getLaneVehicleCount(compassDirection: string, turnDirection: string): number {
    const road = this.intersectionState.roadStatuses.find(
      (road: any) => road.compassDirection === compassDirection
    );
    const lane = road?.roadLineStatus.find((lane: any) =>
      lane.turnDirection.includes(turnDirection)
    );
    return lane?.vehicleCount ?? 0;
  }

  getLaneLightCount(compassDirection: string, turnDirection: string): string {
    const road = this.intersectionState.roadStatuses.find(
      (road: any) => road.compassDirection === compassDirection
    );
    const lane = road?.roadLineStatus.find((lane: any) =>
      lane.turnDirection.includes(turnDirection)
    );
    return lane?.lightColor ?? '';
  }

  stepSimulation() {
    this.intersectionService.stepSimulation().subscribe(() => {
      this.fetchCurrentState();
    });
  }

  restartIntersection() {
    this.intersectionService.restartIntersection().subscribe(() => {
      this.fetchCurrentState();
    });
  }
}
