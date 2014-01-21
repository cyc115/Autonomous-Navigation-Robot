import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int BAND_CENTER, BAND_WIDTH;
	private final int motorStraight = 250, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;	// distance measured by the sensor 
	
	/**
	 * correctionFactor is the multiplier used to modify the rate of change of the motor.
	 * <br> The values are determined by testing.
	 */
	double correctionFactor;
	private final double smallCorrection = 0.7 ;
	private final double smallerCorrection = 0.4;
	private final double bigCorrection =1.7 ;
	private final double hugeCorrection = 30;
	
	public PController(int bandCenter, int bandwith) {
		this.BAND_CENTER = bandCenter;
		this.BAND_WIDTH = bandwith;
		//motor go straight
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		//start motor 
		leftMotor.forward();
		rightMotor.forward();
	}
	
	/**
	 * make movement decisions based on the distance from the wall.
	 *  <br>In this example we have not used the given filter for the gaps, instead, we have chosen a 
	 * correct camera angle for the error from the gap to be less noticeable.
	 * @param distance to the wall,
	 */
	public void processUSData(int distance) {
		
		this.distance = distance;

		int error = distance - BAND_CENTER;
		
		//if WITHIN BAND
		if( Math.abs(error) <= BAND_WIDTH ){
			correctionFactor = bigCorrection ;
		} 
		//right turn 
		else if (error < 0 ){
			correctionFactor = hugeCorrection;
		}
		//if far but not too far 
		else if (error >= 25 && error <= 90){
			correctionFactor = bigCorrection;
		}
		else if (error > 90 ){
			correctionFactor = smallerCorrection;
		}
		//if off but not too far off 
		else {
			correctionFactor = smallCorrection;
		}
		//move motor 
		leftMotor.setSpeed((int) (motorStraight - correctionFactor * error) );
		rightMotor.setSpeed((int) (motorStraight + correctionFactor * error));
		
	}

	
	/**
	 * return distance from the wall obtained by sensor 
	 */
	public int readUSDistance() {
		return this.distance;
	}

	/**
	 * returns the right motor speed 
	 */
	public int getRightMotorSpeed() {
		return rightMotor.getSpeed();
	}

	/**
	 * returns the left motor speed 
	 */
	public int getLeftMotorSpeed() {
		return leftMotor.getSpeed();
	}

}
