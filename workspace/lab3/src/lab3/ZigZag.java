package lab3;

import lejos.nxt.NXTRegulatedMotor;

public class ZigZag extends Thread implements Drivable   {

	private static final int FORWARD_SPEED = 200;
	private static final int ROTATE_SPEED = 130;
	private static boolean isCompleted = false ;
	
	private RobotConfigration configuration ;
	private NXTRegulatedMotor leftMotor, rightMotor;
	private double leftRadius, rightRadius, width;
	
	public ZigZag(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor,
			double leftRadius, double rightRadius, double width) {
		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.width = width ;
		this.rightRadius = rightRadius;
		this.leftRadius = leftRadius;
		
		//start after 1 second 
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	/**
	 * usage : create an object first with the empty constructor,
	 *  then set the configuration with setConfiguration
	 */
	public ZigZag() {

	}
	@Override
	/**
	 * usage : create an object first with the empty constructor, 
	 * then set the configuration with setConfiguration
	 */
	public void setConfiguration(RobotConfigration configuration) {
		this.configuration = configuration;
		this.leftMotor = configuration.getLeftMotor();
		this.rightMotor = configuration.getRightMotor();
		this.width = configuration.getWidth() ;
		this.rightRadius = configuration.getRightRadius();
		this.leftRadius = configuration.getLeftRadius();
		
	}
	public ZigZag(RobotConfigration config){
		this.configuration = config;
		this.leftMotor = config.getLeftMotor();
		this.rightMotor = config.getRightMotor();
		this.width = config.getWidth() ;
		this.rightRadius = config.getRightRadius();
		this.leftRadius = config.getLeftRadius();
	}

	/**
	 * do the drive
	 */
	public void drive (){
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor }) {
			motor.stop();
			motor.setAcceleration(3000);
		}

		// wait 2 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}
		
		//angle in rad
		double firstAngle = Math.atan(Math.toRadians(30)/Math.toRadians(60));//first turning ang 
		double secondAngle =firstAngle - Math.PI  ;
		
		//Procedures
		turn(Math.toDegrees(firstAngle));  //turning angle for the first turn 
		travel(67.082);	//sqrt(30^2 + 60^2) 
		//TODO carry on to finish the procedures 
		
		
	}
	
	@Override
	public void run() {
		this.drive();
		
	}
	/**
	 * travel a distance straight
	 * @param d in cm 
	 */
	private void travel(double d) {
		// drive forward two tiles
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);

		leftMotor.rotate(convertDistance(leftRadius, d), true);
		rightMotor.rotate(convertDistance(rightRadius, d), false);
	}
	/**
	 * make relative turns based on the angle given in deg
	 * @param deg angle [positive :turn counterclockwise] [negative: clockwise]
	 */
	private void turn(double deg) {
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		//assume ang is positive then
		int leftAngle = convertAngle(leftRadius, width, deg);
		int rightAngle = -convertAngle(rightRadius, width, deg);
		//correct the sign of angle
		if (deg < 0){
			//swap angles 
			int temp = leftAngle;
			leftAngle = rightAngle;
			rightAngle =temp;
		}
		
		leftMotor.rotate(leftAngle, true);
		rightMotor.rotate(rightAngle, false);
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

	
	
	public NXTRegulatedMotor getLeftMotor() {
		return leftMotor;
	}
	public void setLeftMotor(NXTRegulatedMotor leftMotor) {
		this.leftMotor = leftMotor;
	}
	public NXTRegulatedMotor getRightMotor() {
		return rightMotor;
	}
	public void setRightMotor(NXTRegulatedMotor rightMotor) {
		this.rightMotor = rightMotor;
	}
	public double getLeftRadius() {
		return leftRadius;
	}
	public void setLeftRadius(double leftRadius) {
		this.leftRadius = leftRadius;
	}
	public double getRightRadius() {
		return rightRadius;
	}
	public void setRightRadius(double rightRadius) {
		this.rightRadius = rightRadius;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public boolean isCompleted() {
		return isCompleted;
	}

	
	
}
