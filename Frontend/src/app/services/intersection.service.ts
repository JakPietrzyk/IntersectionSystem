import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IntersectionStateInterface } from '../interfaces/intersection.state.interface';
import {Vehicle} from '../interfaces/vehicle.interface';

@Injectable({
  providedIn: 'root',
})
export class IntersectionService {
  private apiUrl = 'http://localhost:8080/api/traffic';

  constructor(private http: HttpClient) {}

  fetchCurrentState(): Observable<IntersectionStateInterface> {
    return this.http.get<IntersectionStateInterface>(`${this.apiUrl}/state`);
  }

  stepSimulation(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/step`, {});
  }

  restartIntersection(): Observable<void> {
    return this.http.get<void>(`${this.apiUrl}/restart`);
  }

  addVehicles(vehiclesData: Vehicle[]): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/addVehicle`, vehiclesData)
  }
}
