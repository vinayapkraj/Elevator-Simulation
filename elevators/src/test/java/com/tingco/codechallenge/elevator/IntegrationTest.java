package com.tingco.codechallenge.elevator;

import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.ElevatorApplication;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.events.ElevatorPressEvent;
import com.tingco.codechallenge.elevator.events.FloorPressEvent;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ElevatorApplication.class)
@TestPropertySource(locations = "classpath:integrationtest.properties")
public class IntegrationTest {
	
	@Value("${com.tingco.elevator.test.numberofelevators}")
    private int numberOfElevators;
    
    @Value("${com.tingco.elevator.test.numberoffloors}")
	private int numberOfFloors;
    
    @Value("${com.tingco.elevator.test.numberofevents}")
	private int numberOfEvents;

	@Autowired
	private EventBus eventBus;

	@Autowired
	private ThreadPoolExecutor executor;



	@Test
	public void simulateAnElevatorShaft() throws InterruptedException {
		IntStream.range(0,numberOfEvents).forEach(i -> {
			eventBus.post(new FloorPressEvent(randomFloor(),randomDirection()));
			eventBus.post(new ElevatorPressEvent(randomElevatorId(), randomFloor()));
			i++;
		});
	}
	Random random = new Random();
	private int randomElevatorId() {
		return random.nextInt(numberOfElevators);
	}

	private int randomFloor() {
		return random.nextInt(numberOfFloors);
	}
	
	private Elevator.Direction randomDirection(){
		if(random.nextBoolean()) return Elevator.Direction.UP;
		else return Elevator.Direction.DOWN;
	}

}
