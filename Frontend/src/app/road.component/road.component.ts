import { Component, Input } from '@angular/core';
import {LaneComponent} from '../lane-component/lane.component';
import {MatGridList, MatGridTile} from '@angular/material/grid-list';
import {NgClass, NgIf} from '@angular/common';

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
  @Input() compassDirection!: string;
  @Input() hasLeftLane: boolean = true;
  @Input() hasStraightOrRightLane: boolean = true;

  @Input() getLaneVehicleCount!: (compassDirection: string, turnDirection: string) => number;
  @Input() getLaneLightCount!: (compassDirection: string, turnDirection: string) => string;
  @Input() fetchCurrentState!: () => void;
}
