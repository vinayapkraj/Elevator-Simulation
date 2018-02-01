package com.tingco.codechallenge.elevator.api;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tingco.codechallenge.elevator.api.ElevatorControllerImpl;

public class ElevatorControllerTest {
	@Autowired
	private ElevatorControllerImpl elevatorController;
	private static final  Logger log = LoggerFactory.getLogger(ElevatorControllerTest.class);

	@Before
	public void setUp() {
		try {
			elevatorController = new ElevatorControllerImpl(2, 6);
		}
		catch(Exception e) {
			log.info(" Exception in Elevator controllerTest");
			e.printStackTrace();
		}
	}
	
	@Test
	public void getElevatorAvailableOnFloorTest(){
//		elevatorController = new ElevatorControllerImpl(2, 6);
		Elevator elevator1 = elevatorController.getElevators().get(0);
		Elevator elevator2 = elevatorController.getElevators().get(1);
		
		if(elevator1!=null) {
			elevator1.addInTargetFloorList(1);
			elevator1.serveAllTargets();
		}
		if(elevator2!=null) {
			elevator2.addInTargetFloorList(2);
			elevator2.serveAllTargets();
		}
		Elevator elevatorTest=elevatorController.checkAvailabilityInReqFloor(2);
		if(elevatorTest!=null) {
			int elevatorId = elevatorTest.getId();
			Assert.assertEquals(2, elevatorId);
			log.info("Test Available on Floor expected value 2 -- " + elevatorId);
		}
		else log.info("Test Failed ");
		
	}
	
	@Test
	public void getClosestElevatorTest() {
		Elevator elevator1 = elevatorController.getElevators().get(0);
		Elevator elevator2 = elevatorController.getElevators().get(1);
		List<Elevator> elevatorList = new ArrayList<Elevator>();
		if(elevator1!=null) {
			elevator1.addInTargetFloorList(1);
			elevator1.serveAllTargets();
			elevatorList.add(elevator1);
		}
		
		if(elevator2!=null) {
			elevator2.addInTargetFloorList(2);
			elevator2.serveAllTargets();
			elevatorList.add(elevator2);
		}
		
		Elevator elevator = elevatorController.findClosesetElevator(3,elevatorList);
		Assert.assertNotNull(elevator);
		if(elevator!=null) {
			Assert.assertEquals(2, elevator.currentFloor());
			log.info("Get Closest Elevator Test -- Expected value 2  -- "+ elevator.getId());
		}
		else log.info("ClosestElevator Test Failed");
	}

	@Test
	public void testGetElevatorById() {
		Elevator elevator = elevatorController.getElevators().get(0);
		
		if(elevator!=null) {
			elevator.addInTargetFloorList(3);
			elevator.serveAllTargets();
		}
		Elevator requestedElevator = elevatorController.getElevatorByID(1);
		if(requestedElevator!=null) {
			Assert.assertEquals(elevator.getId(), requestedElevator.getId());
			Assert.assertEquals(3, requestedElevator.currentFloor());
		}
		
		if(elevator!=null)log.info("Test Get Elevator by Id -- "+ elevator.getId());
	}
	
	@Test
	public void testGetElevators() {
		log.info("Get ElevatorsTest " );
//		elevatorController = new ElevatorControllerImpl(2, 6);
		Assert.assertEquals(2, elevatorController.getElevators().size());
		
	}
	
}
