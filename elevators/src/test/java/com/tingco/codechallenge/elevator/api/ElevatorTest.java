package com.tingco.codechallenge.elevator.api;

import org.junit.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ElevatorTest {
    private static final int elevatorId = 1;
    private static final int totalFloor = 4;

    @Test
    public void testMoveElevator() {
        ElevatorImpl elevator = new ElevatorImpl(elevatorId, totalFloor);
        elevator.addInTargetFloorList(2);
        assertThat(elevator.isBusy(), is(true));
        elevator.serveAllTargets(); 
        
        assertThat(elevator.currentFloor(), is(2));
        

     }
    @Test
    public void testAddTargetFloor() {
        Elevator elevator = new ElevatorImpl(elevatorId, totalFloor);
        elevator.addInTargetFloorList(3); 
        
        assertThat(elevator.getTargetFloorList().size(), is(1));
      }
    
}