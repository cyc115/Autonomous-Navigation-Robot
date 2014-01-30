/*
 * SquareDriver.java
 */
import lejos.nxt.*;

public class SquareDriver{
	private static final int FORWARD_SPEED = 150;
	private static final int ROTATE_SPEED = 130;
	private static boolean isCompleted = false ;
	/**
	 * movement up signifies we are in the process of driving up three squares etc ... 
	 */
	public static int MOVEMENT_UP =0, MOVEMENT_RIGHT = 1, MOVEMENT_LEFT =2, MOVEMENT_DOWN =3;
	/**
	 * signifies the current moving direction : either MOVEMENT_UP / DOWN / RIGHT / LEFT 
	 * MOVEMENT_UP =0, MOVEMENT_RIGHT = 1, MOVEMENT_LEFT =2, MOVEMENT_DOWN =3;
	 */
	private static int currentMovementDirection =-1 ;
	

	public static void drive(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor,
			double leftRadius, double rightRadius, double width) {
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor }) {
			motor.stop();
			motor.setAcceleration(3000);
		}

		// wait 5 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}

		for (currentMovementDirection = 0; currentMovementDirection < 4; currentMovementDirection++) {

			
			// drive forward two tiles
			leftMotor.setSpeed(FORWARD_SPEED);
			rightMotor.setSpeed(FORWARD_SPEED);

			leftMotor.rotate(convertDistance(leftRadius, 60.96), true);
			rightMotor.rotate(convertDistance(rightRadius, 60.96), false);

			// turn 90 degrees clockwise
			leftMotor.setSpeed(ROTATE_SPEED);
			rightMotor.setSpeed(ROTATE_SPEED);
			
			leftMotor.rotate(convertAngle(leftRadius, width, 90.0), true);
			rightMotor.rotate(-convertAngle(rightRadius, width, 90.0), false);
		}
		
		isCompleted = true ;
		
	}

	public static int getCurrentMovementDirection() {
		return currentMovementDirection;
	}

	/**
	 * 
	 * @param radius radius of the wheel 
	 * @param distance distance we want to cover 
	 * @return 
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	/**
	 * the 4 square movement is completed
	 * @return 
	 */
	public static boolean isCompleted() {
		return isCompleted;
	}
}