import { Component, Input } from '@angular/core';
import {LaneComponent} from '../lane/lane.component';
import {MatGridList, MatGridTile} from '@angular/material/grid-list';
import {NgClass, NgIf} from '@angular/common';
import {CompassDirection} from "../types/compassDirection.type";
import {TurnDirection} from "../types/turnDirection.type";
import {LightColor} from "../types/lightColor.type";

@Component({
  selector: 'app-road',
  templateUrl: './road.component.html',
  styleUrls: ['./road.component.css'],
  imports: [
    LaneComponent,
    MatGridTile,
    NgIf,
    MatGridList,
    NgClass
  ],
  standalone: true
})
export class RoadComponent {
  @Input() compassDirection!: CompassDirection;
  @Input() hasLeftLane = true;
  @Input() hasStraightOrRightLane = true;

  @Input() getLaneVehicleCount!: (compassDirection: CompassDirection, turnDirection: TurnDirection) => number;
  @Input() getLaneLight!: (compassDirection: CompassDirection, turnDirection: TurnDirection) => LightColor;
  @Input() fetchCurrentState!: () => void;
}
