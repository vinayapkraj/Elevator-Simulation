package com.tingco.codechallenge.elevator;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.ElevatorControllerImpl;
import com.tingco.codechallenge.elevator.events.EventListener;

/**
 * Preconfigured Spring Application boot class.
 *
 */
@SpringBootApplication
@Configuration
@ComponentScan( { "com.tingco.codechallenge.elevator" })
@EnableAutoConfiguration
@PropertySource(value = "classpath:application.properties")
public class ElevatorApplication {

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;
    
    @Value("${com.tingco.elevator.numberoffloors}")
	private int numberOfFloors;

	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	public int getNumberOfFloors() {
		return numberOfFloors;
	}
	
    /**
     * Create a default thread pool for your convenience.
     *
     * @return Executor thread pool
     */
	
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor taskExecutor() {
    	return new ThreadPoolExecutor(numberOfElevators, numberOfFloors,
                5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(numberOfFloors));
    }

    /**
     * Create an event bus for your convenience.
     *
     * @return EventBus for async task execution
     */
    @Bean
    public EventBus eventBus(EventListener eventListener) {
    	EventBus asyncEventBus=new AsyncEventBus(Executors.newCachedThreadPool());
    	asyncEventBus.register(eventListener);
        return asyncEventBus;
    }
    
    @Bean
	public EventListener eventListener(ElevatorControllerImpl elevatorController, ThreadPoolExecutor executor) {
		return new EventListener(elevatorController, executor);
	}
    
    @Bean
	public ElevatorControllerImpl elevatorControllerImpl() {
		return new ElevatorControllerImpl(numberOfElevators, numberOfFloors);
	}
    
    /**
     * Start method that will be invoked when starting the Spring context.
     *
     * @param args
     *            Not in use
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }

}
