package lab3StartFromFresh;
import lejos.nxt.*;

public class PController extends Thread implements UltrasonicController {
	
	private final int BAND_CENTER = 29 , BAND_WIDTH = 3;
	private RobotConfiguration config;
	private final int FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor , rightMotor;	
	private int distance;	// distance measured by the sensor 
	private Planner  planner ;
	private UltrasonicSensor uSensor = new UltrasonicSensor(SensorPort.S1);
	
	PController(RobotConfiguration config, Planner planner){
		this.config = config;
		leftMotor = RobotConfiguration.LEFT_MOTOR;
		rightMotor = RobotConfiguration.RIGHT_MOTOR;
		this.planner = planner;

	}
	
	/**
	 * correctionFactor is the multiplier used to modify the rate of change of the motor.
	 * <br> The values are determined by testing.
	 */
	double correctionFactor;
	private final double smallCorrection = 0.7 ;
	private final double smallerCorrection = 0.4;
	private final double bigCorrection =1.7 ;
	private final double hugeCorrection = 30;
	
	
	public void run(){
		
		//motor go straight
		leftMotor.setSpeed(RobotConfiguration.FORWARD_SPEED);
		rightMotor.setSpeed(RobotConfiguration.FORWARD_SPEED);
		//start motor 
		leftMotor.forward();
		rightMotor.forward();
		while (true){
			if (planner.isWallFollow()){
				processUSData(uSensor.getDistance());
			}
			else{
				config.resetMotorSpeed();
				break;
				//break the control back to planner
			}

		}

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
		leftMotor.setSpeed((int) (RobotConfiguration.FORWARD_SPEED - correctionFactor * error) );
		rightMotor.setSpeed((int) (RobotConfiguration.FORWARD_SPEED + correctionFactor * error));
		
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
