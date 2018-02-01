package com.tingco.codechallenge.elevator.api;

import java.util.List;


/**
 * Interface for the Elevator Controller.
 *
 * @author Sven Wesley
 *
 */
interface ElevatorController {

    
    /**
     * Request an elevator to the specified floor.
     *
     * @param toFloor
     *            addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     * 
     * Vinay -- Included requested Direction for handling Direction logic
     */
    Elevator requestElevator(int toFloor, ElevatorImpl.Direction requestedDirection);

    /**
     * A snapshot list of all elevators in the system.
     *
     * @return A List with all {@link Elevator} objects.
     */
    List<Elevator> getElevators();

    /**
     * Telling the controller that the given elevator is free for new
     * operations.
     *
     * @param elevator
     *            the elevator that shall be released.
     */
    void releaseElevator(Elevator elevator);
    
    /**
	 * Stops the Elevator. Can be used in case of emergency.
	 * Checks the Direction of Elevator and stops in the immediate next floor
	 * Release the elevator when its stopped. 
	 * 
	 */
	
	void stopElevator(int elevatorId);
	
	Elevator requestInsideElevator(int toFloor, int elevatorId);

}
