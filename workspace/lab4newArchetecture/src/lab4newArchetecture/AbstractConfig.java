package lab4newArchetecture;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public abstract class AbstractConfig {
	private Object lock ;
	private Coordinate startLocation;// starting location
	private Coordinate currentLocation ; //current location ;
	private Coordinate nextLocation; //current location ;
	private boolean driveComplete  = false ;
	
	private int ROTATE_SPEED = 100 ;
	private int FORWARD_SPEED = 200;
	//robot hardware
	static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	static final NXTRegulatedMotor SENSOR_MOTOR= Motor.C;
	static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S1;
	static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S2;
	static final double LEFT_RADIUS = 2.090 ;
	static final double RIGHT_RADIUS =2.090;
	static final double WIDTH = 15.24 ;

	/**
	 * 
	 * @return average radius of the wheels of robot 
	 */
	public double getAvgRadius(){
		return (LEFT_RADIUS + RIGHT_RADIUS)/2;
	}

	public void setRotationSpeed(int speed){
		ROTATE_SPEED = speed;
	}
	public void setForwardSpeed(int speed){
		FORWARD_SPEED = speed;
	}
	public int getRotationSpeed(){
		return ROTATE_SPEED;
	}
	public int getForwardSpeed(){
		return FORWARD_SPEED;
	}
	
	public void setDriveComplete(){
		driveComplete = true ;
	}
	public boolean isDriveComplete(){
		return driveComplete;
	}

	public Coordinate getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Coordinate startLocation) {
		this.startLocation = startLocation;
	}

	public Coordinate getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Coordinate currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Coordinate getNextLocation() {
		return nextLocation;
	}

	public void setNextLocation(Coordinate nextLocation) {
		this.nextLocation = nextLocation;
	}

	/**
	 * reset the motot speed to default forward_speed and motor rotation to forward
	 * and start the motor 
	 */
	public void resetMotorSpeed() {
		LEFT_MOTOR.stop();
		RIGHT_MOTOR.stop();
		
		LEFT_MOTOR.setSpeed(FORWARD_SPEED);
		RIGHT_MOTOR.setSpeed(FORWARD_SPEED);
		
		LEFT_MOTOR.forward();
		RIGHT_MOTOR.forward();
	}

	public void stopMotor(){
		LEFT_MOTOR.stop();
		RIGHT_MOTOR.stop();
	}
	/**
	 * this will be called when leftButton is pressed on the brick
	 */
	public abstract void leftButtonPressed();
	/**
	 * this will be called when rightButton is pressed on the brick
	 */
	public abstract void rightButtonPressed();
	
}
