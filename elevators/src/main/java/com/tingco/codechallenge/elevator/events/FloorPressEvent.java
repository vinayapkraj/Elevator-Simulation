package com.tingco.codechallenge.elevator.events;

import com.tingco.codechallenge.elevator.api.ElevatorImpl;

/**
 * This event is generated whenever there is a request for Elevator to move UP / DOWN
 */
public class FloorPressEvent {
	private final int toFloor;
	//private final String requestedDirection;
	private ElevatorImpl.Direction requestedDirection;

	public FloorPressEvent(int toFloor, ElevatorImpl.Direction reqDirection) {
		this.requestedDirection = reqDirection;
		this.toFloor = toFloor;
	}

	public int getRequestToFloor() {
		return toFloor;
	}

	public ElevatorImpl.Direction getRequestedDirection() {
		return requestedDirection;
	}

}