import { Component, Input } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {IntersectionService} from '../services/intersection.service';
import {Vehicle} from '../interfaces/vehicle.interface';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-lane',
  templateUrl: './lane.component.html',
  styleUrls: ['./lane.component.css'],
  imports: [
    NgIf,
    FormsModule,
  ],
  standalone: true,
})
export class LaneComponent {
  @Input() compassDirection!: string;
  @Input() turnDirection!: string;

  @Input() getLaneVehicleCount!: (compassDirection: string, turnDirection: string) => number;
  @Input() getLaneLightCount!: (compassDirection: string, turnDirection: string) => string;
  @Input() fetchCurrentState!: () => void;

  vehicleCount: number = 0;

  constructor(private intersectionService: IntersectionService) {}

  addVehicles(): void {
    const count = Number(this.vehicleCount);

    if (isNaN(count) || count <= 0) {
      alert("Wrong vehicles number")
      return;
    }

    const vehicles: Vehicle[] = [];
    for (let i = 0; i < count; i++) {
      const vehicle: Vehicle = {
        id: uuidv4().toString(),
        startRoad: this.compassDirection,
        turnDirection: this.turnDirection
      };
      vehicles.push(vehicle);
    }

    this.intersectionService.addVehicles(vehicles).subscribe(() => {
      this.vehicleCount = 0;
      this.fetchCurrentState();
    });
  }
}
