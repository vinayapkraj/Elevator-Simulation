## Elevator Simulation Solution:
`Elevator` and `ElevatorController` classes are implemented
`ElevatorControllerEndPoints` and `ElevatorApplication` has been used for starting the application. A Simple GUI is created for simulating and testing the application. Application can also be tested using REST API. Integration and unit testing of the classes has been implemented. Number of floors and number of elevators required for Integration testing can be changed in IntegrationTest.properties available in the resource folder.

Following features has been implemented in this solution.

There are two types of event the program can handle. 
	1. Request for Elevator from floor in UP / DOWN direction
	2. Inside Elevator Request
`ElevatorControllerImpl`  -> This is the core class which handles all events received for elevator.

## Request for Elevator:
This event triggers the service call for 
requestElevator(int toFloor, Direction requestedDirection) in ElevatorControllerImpl.

1. Checks whether any elevator is available in the requested floor.
2. Checks for the elevator in the requested Direction and available in the closest floor
3. If there is no elevator available in requested floor/direction, checks for the idle elevator.
4. If there is no elevator available satisfying the above condition, Thread will sleep for some time and above procedure will be repeated till it finds an elevator for serving the request.

After receiving the Elevator using above condition, the floor is added to the target floor list. As Elevator can serve one or more targets at a time, List of target floor is being maintained.

After adding the target, thread will start running. Target floor is being sorted.

1. If the current direction of the Elevator is UP, then Target Floor will be sorted ascending and min(TargetFloorList) will be served first.

2. If the current direction of the Elevator is DOWN, then Target Floor will be sorted descending and max(TargetFloorList) will be served first.

Once all the floors in the target floor list has been served, the list will be cleared. 


`ElevatorImpl` - this holds the instance of elevator. 

`Move Elevator` - This function is used for moving the elevator to target floor

`GetDirection, setCurrentDirection` - Sets and Returns the current Direction of elevator.

## Inside Elevator Request:

Floor request made inside the elevator, should be in the same direction as the elevator Current direction. 

1. When the elevator is moving UP, requested floor should not be less than current floor.

2. When the elevator is moving down, requested floor should not be greater than current floor.

This feature can be used to disable the floor numbers which are opposed to the current elevator direction.

## How to Run:

1.Download the Jar from elevators/target available in the Github url 
https://github.com/vinayapkraj/Elevator-Simulation.git

2. Open command Prompt, go to the project downloaded location /target.

3. Run the command "java -jar elevator-1.0-SNAPSHOT.jar"

4. Application will be available in the http://localhost:8081/rest/v1/requestElevator/{Floor}/{Diretion}
http://localhost:8081/rest/v1/requestInsideElevator/{ElevatorId}/{Floor}

5. The result of the application can be viewed in the logfile `myelevatorlogfile.log` 

6. Integration test will stimulate 6 request and 6 elevator events and result of the same can be viewed in the above logfile

## How to run Test Angular GUI

1. Open Command prompt, go to the project downloaded location. (Elevator_UI)
2. Run command 'npm install' and 'ng serve'
3. Application will be available in 
	http://localhost:4200
4. GUI can only be used for triggering the events and the result can only be viewed in the logfile `myelevatorlogfile.log` 

This is a sample application that can be used for testing the implemetned solution for 6 elevators and 10 floors.
