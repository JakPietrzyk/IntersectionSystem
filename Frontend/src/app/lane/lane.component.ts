import {Component, Input} from '@angular/core';
import {NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {IntersectionService} from '../services/intersection.service';
import {CreateVehicles} from '../interfaces/vehicle.interface';
import {CompassDirection} from "../types/compassDirection.type";
import {TurnDirection} from "../types/turnDirection.type";
import {LightColor} from "../types/lightColor.type";

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
    @Input() compassDirection!: CompassDirection;
    @Input() turnDirection!: TurnDirection;

    @Input() getLaneVehicleCount!: (compassDirection: CompassDirection, turnDirection: TurnDirection) => number;
    @Input() getLaneLight!: (compassDirection: CompassDirection, turnDirection: TurnDirection) => LightColor;
    @Input() fetchCurrentState!: () => void;

    vehicleToAddCount = 0;

    constructor(private intersectionService: IntersectionService) {
    }

    addVehicles(): void {
        if (this.vehicleToAddCount <= 0) {
            alert("Wrong vehicles number")
            return;
        }
        const createVehicles: CreateVehicles = {
            numberOfVehicles: this.vehicleToAddCount,
            startRoad: this.compassDirection,
            turnDirection: this.turnDirection
        }

        this.intersectionService.addVehicles(createVehicles).subscribe(() => {
            this.vehicleToAddCount = 0;
            this.fetchCurrentState();
        });
    }
}
