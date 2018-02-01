package com.tingco.codechallenge.elevator.api;

import java.util.List;

/**
 * Interface for an elevator object.
 *
 * @author Sven Wesley
 *
 */
public interface Elevator {

    /**
     * Enumeration for describing elevator's direction.
     */
    enum Direction {
        UP, DOWN, NONE
    }

    /**
     * Tells which direction is the elevator going in.
     *
     * @return Direction Enumeration value describing the direction.
     */
    Direction getDirection();

    /**
     * If the elevator is moving. This is the target floor.
     *
     * @return primitive integer number of floor
     */
    int getAddressedFloor();

    /**
     * Get the Id of this elevator.
     *
     * @return primitive integer representing the elevator.
     */
    default int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

    /**
     * Command to move the elevator to the given floor.
     *
     * @param toFloor
     *            int where to go.
     */
    void moveElevator(int toFloor);

    /**
     * Check if the elevator is occupied at the moment.
     *
     * @return true if busy.
     */
    boolean isBusy();

    /**
     * Reports which floor the elevator is at right now.
     *
     * @return int actual floor at the moment.
     */
    int currentFloor();
    
    /**
     * Check whether the userInput is in sync with lift CurrentDirection..
     *
     * @return String Valid / Invalid.
     */
    String checkForValidInput(int inputFloor);
    
    /**
     * Adds the additional request toFloor on its way to target floor
     * 
     * @param toFloor
     */
    
    void addInTargetFloorList(int toFloor);
    
    /**
     * @return List of all the target floors assigned to the lift
     * 
     */
    
    List<Integer> getTargetFloorList();
    
    /**
     * when there is some target to serve for this elevator, this method will be called.
     * 
     * Checks the Direction of Elevator and serve for the consecutive floors in the target floor list
     * 
     */
    
    void serveAllTargets(); 

}
