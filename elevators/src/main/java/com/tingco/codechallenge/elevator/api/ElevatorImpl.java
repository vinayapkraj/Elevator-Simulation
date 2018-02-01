/**
 *Instance of Elevator and its associated functions to return its current state. 
 */
package com.tingco.codechallenge.elevator.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.MoreObjects;

/**
 * @author Vinay
 *
 */
public class ElevatorImpl implements Elevator,Runnable {

	private static final int travelTime=50;
	private static final int passengerTime=100;
	private static final Logger log = Logger.getLogger(ElevatorImpl.class);
	
	private int elevatorId;
	private int currentFloor;
	private int targetFloor;
	private List<Integer> targetFloorList=Collections.synchronizedList(new ArrayList<Integer>());
	private List<Integer> servedFloorList=Collections.synchronizedList(new ArrayList<Integer>());
	private int totalFloors;
	private Direction currentDirection;
	private String errorMsg;

	public ElevatorImpl(int elevatorId,int totalFloors) {
		this.elevatorId=elevatorId;
		if((elevatorId % 2) == 0) this.currentFloor=totalFloors;
		else this.currentFloor=0;
		this.targetFloor=0;
		this.totalFloors=totalFloors;
		this.currentDirection = Direction.NONE;
		this.errorMsg="";
	}

	/** 
	 * Implementation of com.tingco.codechallenge.elevator.api.Elevator#getDirection()
	 * returns the current Direction of Elevator
	 */
	@Override
	public Direction getDirection() {
		return currentDirection;
		
	}

	/** 
	 * 
	 * Implemented com.tingco.codechallenge.elevator.api.Elevator#getAddressedFloor()
	 * AddressedFloor is the final destination point of Elevator
	 * While Moving UP and DOWN its maximum and minimum of the Target floor list respectively
	 * Addressed Floor will be -1 when the elevator is idle
	 * 
	 */
	@Override
	public int getAddressedFloor() {
		int addressedFloor=-1;
		if(isBusy()) {
			switch (currentDirection){
				case UP:
					addressedFloor=targetFloorList.stream().mapToInt(i -> i).max().getAsInt();
					break;
				case DOWN:
					addressedFloor=targetFloorList.stream().mapToInt(i -> i).min().getAsInt();
					break;
				default:
					break;
				
			}
		}
		return addressedFloor;
	}

	/** 
	 * Implemented com.tingco.codechallenge.elevator.api.Elevator#getId()
	 */
	@Override
	public int getId() {
		return elevatorId;
	}

	/**
	 * Implemented com.tingco.codechallenge.elevator.api.Elevator#moveElevator(int)
	 * Elevator moves with Travel time delay and when it is reached the destination, its removed from targetFloorList
	 */
	@Override
	public void moveElevator(int toFloor) {
		if(targetFloor > currentFloor) currentDirection=Direction.UP;
		else {
			if(targetFloor < currentFloor) currentDirection = Direction.DOWN;
			else  currentDirection=Direction.NONE;
		}
		
		
		log.info("Elevator ID " + getId() + " currently in Floor " + currentFloor +  " current Direction is  "+ currentDirection + "  moving to the floor " + targetFloor);
				
		while(currentFloor != toFloor) {
			if(currentDirection==Direction.UP) currentFloor = currentFloor + 1;
			if(currentDirection==Direction.DOWN) currentFloor = currentFloor - 1;
			travelTime();
		}
		targetFloorList.removeIf(toFlr-> (currentFloor==toFloor));
	}

	/** 
	 * Implemented com.tingco.codechallenge.elevator.api.Elevator#isBusy()
	 */
	@Override
	public boolean isBusy() {
		
		return !targetFloorList.isEmpty();
	}

	/* 
	 * Implemented com.tingco.codechallenge.elevator.api.Elevator#currentFloor()
	 */
	@Override
	public int currentFloor() {
		
		return currentFloor;
	}
	
	/** 
	 * Elevator serving all the assigned Targets
	 */
	@Override
	public void serveAllTargets() {
		
		while((!targetFloorList.isEmpty()) && (targetFloorList.size() > 0 ) ) {
			if(currentDirection==Direction.UP) {
				if(targetFloorList.stream().mapToInt(i -> i).min().isPresent())
					targetFloor=targetFloorList.stream().mapToInt(i -> i).min().getAsInt();
				else targetFloor=targetFloorList.get(0);
				
				moveElevator(targetFloor);
				servedFloorList.add(targetFloor);
				passengerTime();
			}
			if(currentDirection==Direction.DOWN) {
				if(targetFloorList.stream().mapToInt(i -> i).max().isPresent())
					targetFloor=targetFloorList.stream().mapToInt(i -> i).max().getAsInt();
				else targetFloor=targetFloorList.get(0);
				
				moveElevator(targetFloor);
				servedFloorList.add(targetFloor);
				passengerTime();
			}
			targetFloorList.removeIf(toFlr-> (currentFloor==targetFloor));
			log.info("Elevator ID  " + getId() + " reached floor " + targetFloor );
			
			if(targetFloor==0 || targetFloor == totalFloors) {
				if (currentFloor == totalFloors) currentDirection=Direction.DOWN;
				if (currentFloor == 0) currentDirection=Direction.UP;
				if(targetFloorList.size() == servedFloorList.size()) currentDirection=Direction.NONE;
				targetFloorList.clear();
				servedFloorList.clear();
			}
			
		}
		
	}
	
	/**
	 * Implemented com.tingco.codechallenge.elevator.api.Elevator#addInTargetFloorList(int)
	 * Adds toFloor to Target Floor List
	 */
	@Override
	public void addInTargetFloorList(int toFloor) {
//		log.info("Elevator " + getId() + " current Direction is " + getDirection() + "moving towards " + toFloor + " Floor");
		this.targetFloorList.add(toFloor);
		setCurrentDirection(toFloor);
	}
	
	/**
	 * Sets the current Direction of Elevator
	 */
	public void setCurrentDirection(int toFloor) {
		if(currentFloor < toFloor) {
			currentDirection = Direction.UP;
		}
			
		else if (currentFloor > toFloor) {
			currentDirection = Direction.DOWN;
		}
		else
			currentDirection= Direction.NONE;
	}
	
	/**
	 * Returns the List of Target Floors assigned for the Elevator.
	 */
	@Override
	public List<Integer> getTargetFloorList() {
		return this.targetFloorList;
	}
	/** 
	 * Makes the thread sleep till the travelTime for moving between consecutive floors
	 */	
	private void travelTime() {
		try {
//			log.info("Elevator " + getId() +"  is moving" );
			Thread.sleep(travelTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * Makes the thread sleep till the passengerTime for passengers to get Down
	 */	
	private void passengerTime() {
		try {
			log.info("Elevator " + getId() +"  is waiting for Passengers to get down" );
			Thread.sleep(passengerTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * For verifying whether userInput is in sync with lift CurrentDirection.
	 * This can be used in Simulation to prevent the user to press the button which are not in current Direction.
	 */
	@Override
	public String checkForValidInput(int inputFloor) {
		errorMsg="";
		currentDirection=getDirection();
		if((currentDirection==Direction.UP && inputFloor<currentFloor) || (currentDirection==Direction.DOWN && inputFloor>currentFloor))
				errorMsg+="Invalid Input";
		else 
			errorMsg="valid";
		return errorMsg;		
	}
	

	@Override
	public void run() {
		this.targetFloorList=getTargetFloorList();
		while (isBusy()) {
			serveAllTargets();
		}
		
	}

}
