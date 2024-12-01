
# IntersectionSystem
The IntersectionSystem is a Java-based application designed to efficiently manage traffic at intersections. It models real-world traffic systems, incorporating features such as traffic light control, road configurations, and vehicle management to ensure smooth and organized traffic flow.

## Running the Project
The project can be executed in multiple ways, depending on your requirements:

### Running the Simulation with Command-Line Arguments
Run the Main.class with the following arguments to execute a simulation:

```shell
mvn clean package
java -jar target/IntersectionSystem-1.0-SNAPSHOT.jar -sim -it intensity | sequential -i input.json -o output.json


-sim: Starts the simulation mode.
-it intersection: Specifies the intersection type
-i simulationFiles/input.json: Provides the input file for the simulation commands.
-o simulationFiles/output.json: Specifies the output file to store the simulation results.
```

### Running the java Main Class and Interface
Intersection type can be set in application.yml file.

Simply execute the Main.class without any arguments. This will start the application as an HTTP server, allowing it to receive and respond to HTTP requests.

Additionaly Angular application can be started to launch interface in the browser.

### Running with Docker Compose
Use the provided docker-compose.yml file to launch the project. This method will not only start the server but also spin up a simple Angular-based web interface for interacting with the system.

```shell
docker-compose up
```
Once the services are up, you can access the web interface in your browser to control and monitor the traffic intersection system visually.


## Algorithm Overview
Incoming vehicles are assigned to specific lanes based on their desired direction (e.g., left, straight, or right).
Vehicles wait at the intersection until their lane’s traffic light turns green.

### Intersections
The code allows the user to define custom road setups. From within the code, you can specify any type of roads and lane configurations, and the algorithm will adapt accordingly to provide the most optimal results based on the given setup. Although interface currently supports only one predefined road setup.

### Sequential Intersection
The traffic lights operate in predefined cycles, alternating between directions and types of movements as follows:
- NORTH + SOUTH : STRAIGHT + RIGHT,
- NORTH + SOUTH : LEFT,
- EAST + WEST : STRAIGHT + RIGHT,
- EAST + WEST : LEFT
  The system does not monitor the number of vehicles waiting, and the sequence repeats in this order.
### Intensity Monitoring Intersection
- The traffic lights operate in adaptive cycles, dynamically adjusting based on the traffic intensity on each road. The system prioritizes clearing lanes with higher vehicle density to minimize congestion.

- To prevent starvation, if any road line remains non-empty and vehicles have been waiting longer than a predefined threshold, the system ensures that the next light switch accommodates the starved direction.

- The system is designed to maximize efficiency by activating non-colliding directions whenever possible. For instance, when a starved direction is prioritized, the algorithm will attempt to identify all non-colliding directions that can be handle.

- If only one road has vehicles waiting, the lights will remain unchanged until another road accumulates more vehicles, or a roadline with at least one vehicle will exceed starvation threshold.

- If a road has multiple lanes designated for the same direction (e.g., multiple left-turn lanes), all such lanes will simultaneously receive a green light.


## Components Overview
1. Interface
   Additionally, an interface was created to allow manual management of the intersection. This interface supports a single road setup, identical to the Sequential Intersection cycle. Through the interface, users can control the traffic lights and manage the intersection manually.

User is able to:
- add vehicle on each road line (id of the vehicle is generated randomly),
- make step of simulation,
- restart the intersection to initial phase.

2. TrafficController Class
   Provides REST API endpoints for interacting with the system:

Adds a vehicle to a specific road.
```
POST /api/traffic/addVehicle
```
Retrieves the current status of the intersection.
```
GET /api/traffic/state
```
Advances the simulation by one time step.
```
POST /api/traffic/step
```
Resets the intersection to its initial state.
```
GET /api/traffic/restart
```

3. CommandsLineParser
   Responsible for parsing arguments provided from command line

4. SimulationRunner
   Creates intersection model based of provided arguments, then parses commands included in input file to execute all commands. After all it saves all left vehicles to outputfile.

5. TrafficLights
   Manages the state of traffic lights only GREEN and RED lights are supported.

6. VehicleFlowController
   Responsible for controling vehicles on roads. Checks for green light if it it it removes vehicle from road.

7. LightsController
   Responsible for core logic for traffic management. It has two implementations:
- SequentialLightsFlowController
- LightsControllerWithIntensityMonitoring
  Based on implementation it commands TrafficLightsSwitcher to switch lights properly.

8. DirectionSelector
   Used in LightsControllerWithIntensityMonitoring to pick direction to handle. It uses:
- MostStarvedDirectionFinder - to find starved directions
- MostOverloadedDirectionFinder - to find directions with most vehicles

9. Intersection
   Used to gather all components up. All intersection implementations must have road for each compass direction. Each road consists of Roadlines with TrafficLights. Each roadline can have more then one turn direction possible:
- StraightOrRightRoadLine - allows to "turn" STRAIGHT or RIGHT
- LeftTurnRoadLine - allows only to turn LEFT


## Further options to develop
## Interface
- I mainly focused on backend development, but I also wanted to include a simple frontend application to visually present the algorithm. The code is structured into smaller components to allow dynamic creation of intersections.

- Although I found an Angular component called mat-grid, I believe it was not the ideal choice. I spent some time trying to reduce repeated code, but using ng-container with ng-template didn’t work as expected with mat-grid.

- Additionally, instead of passing functions from intersection.component to lower-level components, they should be moved to a proper service to improve separation of concerns and maintainability.

- Furthermore, the line:

```
Vehicles: <span>{{ getLaneVehicleCount(compassDirection, turnDirection) }}</span>
```
is not an optimal approach for the browser, as it does not leverage observables. It should be changed to an observable to make it more efficient and reactive. Unfortunately, due to time constraints, I wasn’t able to make this change.