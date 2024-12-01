import {Component} from '@angular/core';
import {IntersectionComponent} from './intersection/intersection.component';

@Component({
    selector: 'app-root',
    imports: [
        IntersectionComponent,
    ],
    templateUrl: './app.component.html',
    standalone: true,
    styleUrl: './app.component.css'
})
export class AppComponent {
    title = 'IntersectionSystemAngular';
}
