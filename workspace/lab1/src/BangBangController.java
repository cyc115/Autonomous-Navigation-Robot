import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	
	private final int BAND_CENTER, 	// ideal distance from the wall 
						BANDWIDTH;	// The epsilon 
	private final int MOTOR_LOW, MOTOR_HIGH; 	//speed of motors

	private final int MOTOR_STRAIGHT = 200;//speed of the motor when going straight 
	private final int INCREMENT = MOTOR_STRAIGHT/20; //5% INCREMENT
	
	private final NXTRegulatedMotor 
			leftMotor = Motor.A,
			rightMotor = Motor.C;

	private final NXTRegulatedMotor sensorMotor = Motor.B; 	//Motor used to move sensor
	
	private int lftDist;	//distance measured from left wall	
	private int topDist;	//dist measured from top wall 
	private int currentDist; 	//stores the latest distance reading, should be equal to either one of the above two 
	private int currentLeftSpeed;	
	
	public BangBangController(int bandCenter, int bandwith, int motorLow, int motorHigh) {
		//Default Constructor
		this.BAND_CENTER = bandCenter;
		this.BANDWIDTH = bandwith;
		this.MOTOR_LOW = motorLow;
		this.MOTOR_HIGH = motorHigh;
		leftMotor.setSpeed(MOTOR_STRAIGHT);
		rightMotor.setSpeed(MOTOR_STRAIGHT);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
	}
	
	/**
	 * Fault tolerance for gaps not yet implemented.
	 * Sensor motion not yet implemented
	 */
	public void processUSData(int distance) {
		while (true){
			//first rotate to default (left facing location)
			sensorMotor.rotateTo(-90);
			
		}
		
		
		

/*
		while (true){
			//first rotate to default (left facing location)
			sensorMotor.rotateTo(-90);
			this.distance = distance;
			//if too far
			if ((distance - BAND_CENTER ) > BANDWIDTH){
				leftMotor.setSpeed(leftMotor.getSpeed() + INCREMENT);
				rightMotor.setSpeed(rightMotor.getSpeed() - INCREMENT);
			}
			//if too near
			else if ((distance - BAND_CENTER) < BANDWIDTH){
				leftMotor.setSpeed(leftMotor.getSpeed() - INCREMENT);
				rightMotor.setSpeed(rightMotor.getSpeed() + INCREMENT);
			}
			else{
				//do nothing
				}
			
			//rotate to front 
			sensorMotor.rotateTo(0);
			if 
		}
		
*/

		
		
		
	}
	
	/**
	 * move sensor left 90 deg
	 */
	private void moveSensorLeft(){
		sensorMotor.rotate(-90);
	}
	
	/**
	 * move sensor right 90 deg
	 */
	private void moveSensorRight(){
		sensorMotor.rotate(90);
	}
	
	public int readUSDistance() {
		return this.currentDist;
	}
}
