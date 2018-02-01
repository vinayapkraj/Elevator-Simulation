/**
 * 
 */
package com.tingco.codechallenge.elevator.api;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.*;
import org.springframework.stereotype.Service;

import com.tingco.codechallenge.elevator.api.Elevator.Direction;

import ch.qos.logback.core.net.SyslogOutputStream;

/**
 * @author Vinay
 *
 */

@Service
public class ElevatorControllerImpl implements ElevatorController {

	private static final  Logger log = LoggerFactory.getLogger(ElevatorControllerImpl.class);
	private static final int waitTime = 100;
	private final List<Elevator> elevatorsList = Collections.synchronizedList(new ArrayList<>());
	private final int totalFloors;
	private final int totalElevators;
	

	public ElevatorControllerImpl(int totalElevators , int totalFloors) {
		this.totalElevators=totalElevators;
		this.totalFloors = totalFloors;
		log.info("Started " + totalElevators +" elevators");
		IntStream.rangeClosed(1, totalElevators).forEach(elevatorId -> elevatorsList.add(new ElevatorImpl(elevatorId, totalFloors)));
	}
	
	public int getTotalFloors() {
		return totalFloors;
	}
	public int getTotalElevators() {
		return totalElevators;
	}
	/* 
	 * Implemented com.tingco.codechallenge.elevator.api.ElevatorController
	 * Checks whether elevator available in requested Floor, Else
	 * Check whether Closest elevator available in requested Direction, Else
	 * Check whether any Idle Elevator available, Else
	 * Wait for some time and retry until it gets an elevator
	 * 
	 */
	@Override
	public Elevator requestElevator(int toFloor, ElevatorImpl.Direction reqDierction) {
		Elevator servingElevator=null;
		while(servingElevator==null) {
			servingElevator=checkAvailabilityInReqFloor(toFloor);
			if(servingElevator!=null){
				log.info("Elevator Id " + servingElevator.getId() + "available in requested Floor "+ toFloor );
			}
			else {
				servingElevator=findElevatorInReqDirection(toFloor,reqDierction);
				if(servingElevator!=null) { 
//					log.info("Elevator Id " + servingElevator.getId() + "available closest to Floor "+ toFloor + " current floor " +servingElevator.currentFloor());
				}
				else retryAfterWaitTime();
			}
		}		
		return servingElevator;
	}
	@Override
	public Elevator requestInsideElevator(int toFloor, int elevatorId) {
		Elevator elevator=getElevatorByID(elevatorId);
		if(elevator!=null) {
			String errorMessage = elevator.checkForValidInput(toFloor);
			if(errorMessage.equalsIgnoreCase("valid")) {
				 return elevator; 
			}
			else return null;
		}
		else 
		return null;
	}
	
	public Elevator getElevatorByID(int elevatorId) {
		return getElevators().stream()
                .filter(elevators -> (elevators.getId()==elevatorId)).findFirst().orElse(null);
	}

	/*
	 * Returns random idle Elevator and null in case of unavailability
	 */
	public Elevator getIdleElevator(int toFloor){
		getElevators().stream().forEach(System.out::println);
		return getElevators().stream().
				filter(elevator -> elevator.getTargetFloorList().isEmpty()).findAny().orElse(null);
		
	}
	
	/*
	 * Check whether Elevator is available in requested Floor.
	 * Returns null if it is not available
	 */
	
	public Elevator checkAvailabilityInReqFloor(int toFloor) {
		return getElevators().stream()
                .filter(elevator -> (!elevator.isBusy() && elevator.currentFloor() == toFloor))
                .findAny()
                .orElse(null); 		
	}
	
	/*
	 * Check whether any closest Elevator is available in requested Direction.
	 * If multiple Elevator available in requested direction, return the nearest elevator
	 * Returns null in case of unavailability
	 */
	
	public Elevator findElevatorInReqDirection(int toFloor, ElevatorImpl.Direction reqDirection) {
		int finalDiff=100;
		int diff=0;
		Elevator closestElevator = null;
		for (Elevator elevator:elevatorsList) {
			diff= abs(elevator.currentFloor() - toFloor);
			if (diff < finalDiff ) {
				finalDiff = diff ;
				if(elevator.isBusy()) {
					if ((((elevator.getDirection() == reqDirection.DOWN) && elevator.currentFloor() > toFloor) ||
                					 ((elevator.getDirection()== reqDirection.UP) && elevator.currentFloor() < toFloor))) {
						closestElevator=elevator;						
					}
				}
				else {
					closestElevator=elevator;
				}
			}
		}
		
		if(closestElevator!=null) {
			log.info("Closest Elevator ID " + closestElevator.getId() + " current direction " + closestElevator.getDirection() + " currently in floor " + closestElevator.currentFloor() + " to floor " + toFloor);
			return closestElevator;
		}
		else return null;
	}
	
	public Elevator findClosesetElevator(int toFloor, List<Elevator> elevatorsList) {
		int finalDiff=100;
		Elevator closestElevator = null;
		for (Elevator elevator:elevatorsList) {
			int diff=abs(elevator.currentFloor() - toFloor);
			if(diff<finalDiff) {
				finalDiff=diff;
				closestElevator=elevator;
			}
		}
		return closestElevator;
	}
	
	private void retryAfterWaitTime() {

		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * Implemented com.tingco.codechallenge.elevator.api.ElevatorController#getElevators()
	 */
	@Override
	public List<Elevator> getElevators() {
		return elevatorsList;
	}

	/* 
	 * Implemented com.tingco.codechallenge.elevator.api.ElevatorController
	 * Remove All the target from Target Floor List. This will make the Elevator Idle
	 * 
	 */
	@Override
	public void releaseElevator(Elevator elevator) {
		if(!elevator.getTargetFloorList().isEmpty()) {
			elevator.getTargetFloorList().clear();
		}
		log.debug("Elevator Id " + elevator.getId() + "  is released");

	}
	/*
	 * Stops the Elevator. Can be used in case of emergency.
	 * Checks the Direction of Elevator and stops in the immediate next floor
	 * Release the elevator when its stopped. 
	 * 
	 */
	@Override
	public void stopElevator(int elevatorId) {
		int stoppingFloor=0;
		Elevator elevator=(Elevator) elevatorsList.stream()
                .filter(elevators -> (elevators.getId()==elevatorId));
		if(elevator!=null && elevator.getDirection()==ElevatorImpl.Direction.UP) elevator.addInTargetFloorList(stoppingFloor=elevator.currentFloor()+1);
		if(elevator!=null && elevator.getDirection()==ElevatorImpl.Direction.DOWN) elevator.addInTargetFloorList(stoppingFloor=elevator.currentFloor()-1);
		while(elevator!=null && elevator.getTargetFloorList().contains(stoppingFloor)) {
			retryAfterWaitTime();
		}
		releaseElevator(elevator);
		log.debug("Elevator "+ elevator.getId()+ " stopped in " + stoppingFloor + " Floor");
	}
}
