package com.tingco.codechallenge.elevator.resources;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.*;
import com.tingco.codechallenge.elevator.events.*;


//import net.sf.ehcache.search.Direction;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {

	private static final Logger log = LoggerFactory.getLogger(ElevatorControllerEndPoints.class);
	private EventBus eventBus;
	private ElevatorControllerImpl elevatorControllerImpl;
	
	@Autowired
	public ElevatorControllerEndPoints(ElevatorControllerImpl elevatorController, EventBus eventBus) {
		this.elevatorControllerImpl = elevatorController;
		this.eventBus = eventBus;
	}
	
    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {

        return "pong";
    }
    
    /**
     * 
     * @param floor
     * @param reqDirection
     *  Triggers Elevator event and check for the availability of Elevator
     */
    
    @RequestMapping(value = "/requestElevator/{floor}/{reqDirection}", method = RequestMethod.GET)
    public String requestElevator(@NotNull @PathVariable Integer floor,@NotNull @PathVariable String reqDirection) {
    	log.info("Floor Press Event" + " to floor "+floor+" Requested Direction is " + reqDirection);
	    eventBus.post(new FloorPressEvent(floor,Elevator.Direction.valueOf(reqDirection)));
	    return "Success";
    }
    
    /**
     * 
     * @param elevatorId
     * @param floor
     * @return
     * Triggers Inside Elevator Event
     */
    
    @RequestMapping(value = "/requestInsideElevator/{elevatorId}/{floor}", method = RequestMethod.GET)
    public String requestInsideElevator(@NotNull @PathVariable Integer elevatorId,@NotNull @PathVariable Integer floor) {
    	log.info("Elevator Event " + "Inside Elevator "+elevatorId+ " to floor "+ floor);
    	eventBus.post(new ElevatorPressEvent(elevatorId,floor));
	    return "Success";
    }
    
      
    public boolean validateFloorNumber(int floor) {
		if (floor > elevatorControllerImpl.getTotalFloors() || floor < 0) {
			log.debug("Floor number not valid, it should be between 0 and " + elevatorControllerImpl.getTotalFloors());
			return false;
		}
		else return true;
	}

	public boolean validateElevatorId(int elevatorId) {
		if (elevatorId > elevatorControllerImpl.getTotalElevators() || elevatorId < 1) {
			log.debug("ElevatorId is not valid, it should be between 0 and " + elevatorControllerImpl.getTotalElevators());
			return false;
		}
		else return true;
	}
	
	private boolean validateDirection(String reqDirection) {
		if ((Elevator.Direction.UP.toString()).equalsIgnoreCase(reqDirection) || (Elevator.Direction.UP.toString()).equalsIgnoreCase(reqDirection)) {
			log.debug("Invalid Direction, should be UP / DOWN ");
			return false;
		}
		else return true;
	}
    
}
