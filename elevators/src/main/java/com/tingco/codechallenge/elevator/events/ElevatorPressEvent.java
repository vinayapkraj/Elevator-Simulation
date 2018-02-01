package com.tingco.codechallenge.elevator.events;

/**
 * This event will be triggered when floor option is chosen inside elevator
 */
public class ElevatorPressEvent {
	private final int toFloor;
	private final int elevatorId;

	public ElevatorPressEvent(int elevatorId, int requestToFloor) {
		this.elevatorId = elevatorId;
		this.toFloor = requestToFloor;
	}

	public int getToFloor() {
		return toFloor;
	}

	public int getElevatorId() {
		return elevatorId;
	}

}