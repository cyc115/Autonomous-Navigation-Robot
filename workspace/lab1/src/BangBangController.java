import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwith;
	private final int motorStraight = 100;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	public BangBangController(int bandCenter, int bandwith, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
	}
	
	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		//Just right 
		if ( Math.abs(distance - bandCenter) < bandwith )
		{
			//do nothing 
		}
		//make correction 
		else
		{	double speedFactor = 1.4; //control the overall speed 
			int fast = 270 ; //control the correction 250
			int slow = 110 ;  //150
			
			/**
			 * the distance where we're probably facing a wall. 
			 */
			int criticalDistance = 23;
			
			if (distance < criticalDistance ){
				rightMotor.setSpeed( 10 );
				leftMotor.setAcceleration(fast );
			}
			//too close -- go right
			else if (distance < bandCenter ) {
				rightMotor.setSpeed((int) (slow *speedFactor) ); 
				leftMotor.setSpeed((int) (fast * speedFactor) ); 
			}
			// too far -- go left 
			else if (distance > bandCenter ){
				rightMotor.setSpeed((int) (fast * speedFactor) ); 
				leftMotor.setSpeed((int) (slow *speedFactor)); 
			}  
			else {
				System.out.println("error");
			}
				
		}
		
		
		
	}

	public int getLeftMotorSpeed(){
		return leftMotor.getSpeed();
	}
	public int getRightMotorSpeed(){
		return rightMotor.getSpeed();
	}
	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
