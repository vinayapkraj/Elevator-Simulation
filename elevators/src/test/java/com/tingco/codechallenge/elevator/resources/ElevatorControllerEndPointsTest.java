package com.tingco.codechallenge.elevator.resources;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tingco.codechallenge.elevator.ElevatorApplication;
import com.tingco.codechallenge.elevator.api.ElevatorControllerImpl;
import com.tingco.codechallenge.elevator.resources.*;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ElevatorApplication.class)
@TestPropertySource(locations = "classpath:integrationtest.properties")
public class ElevatorControllerEndPointsTest {

    @Autowired
    private ElevatorControllerEndPoints endPoints;
    

    @Test
    public void ping() {

        Assert.assertEquals("pong", endPoints.ping());

    }

    @Test
    public void requestElevatorTest() {
    	Assert.assertEquals("Success",endPoints.requestElevator(1, "UP"));
    }
    
    @Test
    public void requestInsideElevatorTest() {
    	Assert.assertEquals("Success",endPoints.requestInsideElevator(1, 3));
    }

    @Test
	public void validateInputFloor(){
    	Assert.assertFalse(endPoints.validateFloorNumber(-1));
	}

}
