import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	
	private final int BAND_CENTER, 	// ideal distance from the wall 
						BANDWIDTH;	// The epsilon 
	private final int MOTOR_LOW, MOTOR_HIGH; 	//speed of motors
	
	private final int MOTOR_STRAIGHT = 200;//speed of the motor when going straight 
	
	private final NXTRegulatedMotor 
			leftMotor = Motor.A,
			rightMotor = Motor.C;
	
	private int distance;		
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
	
	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		//if error is small
		if (Math.abs(distance - BAND_CENTER ) < BANDWIDTH){
//			leftMotor.s
		}
		
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
