package com.tingco.codechallenge.elevator.events;

import com.google.common.eventbus.*;
import com.tingco.codechallenge.elevator.api.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.concurrent.*;

@Service
public class EventListener {
	private static final Logger log = LoggerFactory.getLogger(EventListener.class);

	private final ElevatorControllerImpl elevatorController;

	private final ThreadPoolExecutor executor;
	private String eventMessage;

	@Autowired
	public EventListener(ElevatorControllerImpl elevatorController, ThreadPoolExecutor executor) {
		this.elevatorController = elevatorController;
		this.executor = executor;
		this.eventMessage="";
	}

	/**
	 * Subscribed function for handling the request for elevator from any floor to any direction
	 * @param floorPressRequest contains the value of toFloor and requested direction
	 * @return Event Message - Message about elevator going to serve for request
	 */
	
	@Subscribe
	@AllowConcurrentEvents
	public void floorButtonPressed(FloorPressEvent floorPressRequest) {
		log.info("Request from floor " + floorPressRequest.getRequestToFloor() + " in the direction " + floorPressRequest.getRequestedDirection());
		ElevatorImpl elevator =  (ElevatorImpl)elevatorController.requestElevator(floorPressRequest.getRequestToFloor(),floorPressRequest.getRequestedDirection());
		if (elevator != null) {
			elevator.addInTargetFloorList(floorPressRequest.getRequestToFloor());	
			executor.execute(elevator);
		}
		else eventMessage="Failed";
	}

	/**
	 * Subscribed function for handling the request inside elevator to any floor
	 * @param elevatorPressEvent contains Elevator ID and toFloor
	 * @return Event Message
	 */

	@Subscribe
	@AllowConcurrentEvents
	public void requestInsideElevator(ElevatorPressEvent elevatorPressEvent) {
		log.info("Elevator Event- Inside Elevator " + elevatorPressEvent.getElevatorId() + " moving to floor " + elevatorPressEvent.getToFloor());
		ElevatorImpl elevator =  (ElevatorImpl)elevatorController.requestInsideElevator(elevatorPressEvent.getToFloor(),elevatorPressEvent.getElevatorId());
		if(elevator != null) {
			elevator.addInTargetFloorList(elevatorPressEvent.getToFloor());	
			executor.execute(elevator);
			eventMessage="Elevator inside event "+ elevator.getId() + "moving to "+ elevatorPressEvent.getToFloor()  + "floor";
		}
		else eventMessage="Invalid Input";
		
	}
	
	public String getEventMessage() {
		return eventMessage;
	}

}
