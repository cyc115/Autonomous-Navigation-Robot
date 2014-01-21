import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int BAND_CENTER, BAND_WIDTH;
	private final int motorStraight = 250, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	
	public PController(int bandCenter, int bandwith) {
		//Default Constructor
		this.BAND_CENTER = bandCenter;
		this.BAND_WIDTH = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		// rudimentary filter
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		
		
		// TODO: process a movement based on the us distance passed in (P style)
		
//		only move the left motor 
		double correctionFactor;
		double smallCorrection = 0.7 ;
		double smallerCorrection = 0.4;
		double bigCorrection =1.7 ;// 1.3;
		int error = distance - BAND_CENTER;
	/*	
		//set corresponding correction Factor 
		if (error < 0 ){
			correctionFactor = 2.0;
		}

		*/
		
		//if WITHIN BAND
		if( Math.abs(error) <= BAND_WIDTH ){
			correctionFactor = bigCorrection ;
		} 
		//right turn 
		else if (error < 0 ){
			correctionFactor = 30.0 ; // larger 
		}
		//if too far
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
		
		leftMotor.setSpeed((int) (motorStraight - correctionFactor * error) );
		rightMotor.setSpeed((int) (motorStraight + correctionFactor * error));
//		rightMotor.setSpeed(motorStraight);
		
		
		
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

	@Override
	public int getRightMotorSpeed() {
		// TODO Auto-generated method stub
		return rightMotor.getSpeed();
	}

	@Override
	public int getLeftMotorSpeed() {
		// TODO Auto-generated method stub
		return leftMotor.getSpeed();
	}

}
