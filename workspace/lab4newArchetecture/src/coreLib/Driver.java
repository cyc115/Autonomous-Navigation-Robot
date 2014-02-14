package coreLib;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

/**
 * driver class that has the control of motor 
 * TODO solve the movement conflicts  with two calls to different movement 
 * @author yuechuan
 *
 */
public class Driver extends Thread{
	private AbstractConfig config ;
	private Coordinate currentCoordinate, startCoord , endCoord;
	private NXTRegulatedMotor leftMotor , 
								rightMotor ;
	private static Driver instance ;
	private Object lock ;
	
	/**
	 * do not use this to initialize another instance. only used for extension 
	 * @param config
	 */
	@Deprecated
	public Driver(AbstractConfig config){
		this.config = config ;
		leftMotor = AbstractConfig.LEFT_MOTOR;
		rightMotor = AbstractConfig.RIGHT_MOTOR;
		
		currentCoordinate = config.getCurrentLocation();
		endCoord = config.getStartLocation();
	}
	
	
	
	public static Driver getInstance(){
		if (instance == null){
			instance = new Driver(AbstractConfig.getInstance());
		}
		return instance;
	}

	public void travelTo(int x, int y){
			travelTo(new Coordinate(x, y, 0));			
	}
	
	/**
	 * travel to wrt to the global (0,0) coordinate
	 * . Since this method use currentCoordinate which is \
	 * initialized during object initialization. this method is 
	 * made an instance method to avoid undefined behavior.
	 * 
	 * FIXME this method must be complete before any action to change the motor movement 
	 * or else the odo coordinate will be screwed up
	 * @param nextLocationg
	 */
	public void travelTo(Coordinate nextLocation) {
//		synchronized (lock){
			AbstractConfig config = AbstractConfig.getInstance();
			config.setNextLocation(nextLocation);
			config.setStartLocation(currentCoordinate.clone());
//		}
		
		double distance = Coordinate.calculateDistance(currentCoordinate, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentCoordinate, nextLocation);

		RConsole.println("Driver:travelTo:CurrentCoord: " + currentCoordinate.toString());
		RConsole.println("Driver:travelTo:NxtCoord: " + nextLocation.toString());
		RConsole.println("Driver:travelTo:traveling dist: " + distance);
		RConsole.println("Driver:travelTo:turning Angle: " + turningAngle);
	//make turn
		rotateToRelatively(turningAngle);
		setSpeed(config.getForwardSpeed());

		forward(distance);

		/*
		Coordinate temp = new Coordinate(
			nextLocation.getX(), nextLocation.getY() ,
			Coordinate.normalize((currentCoordinate.getTheta() + turningAngle))
			);
		currentCoordinate = temp ;
		
		*/
		RConsole.println("Driver:travelTo:currentCoordinate : x " + config.getCurrentLocation().getX()
			+"\ty " + config.getCurrentLocation().getY() 
			+ "\ttheata " +config.getCurrentLocation().getTheta());
		RConsole.println("=======");
	}
		
	/**
	 * move wheel forward at the same speed it was running at before 
	 * @param dist
	 */
	private void forward(double dist){
//		synchronized(lock){
			leftMotor.rotate(
				convertDistance(AbstractConfig.LEFT_RADIUS, dist), 
				true
				);
			rightMotor.rotate(
				convertDistance(AbstractConfig.RIGHT_RADIUS, dist), 
				false
				);
//		}

	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 *	<br> this method is here only for comparability.
	 *	<br> use rotateToRelatively (doubel degree , boolean returnRightAway);
	 *	when ever possible 
	 * @param degree
	 */
	public void rotateToRelatively(double degree){
		rotateToRelatively(degree, false);			
	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 * @param degree 
	 * @param returnRightAway should the function finish before finishing the turn 
	 */
	public void rotateToRelatively(double degree, boolean returnRightAway){

		rightMotor.setSpeed(AbstractConfig.getInstance().getRotationSpeed());
		leftMotor.setSpeed(AbstractConfig.getInstance().getRotationSpeed());
//		synchronized(lock){
	        if (degree < 0){		//if degree is negative then rotate back ward
	        	leftMotor.backward();
	        	rightMotor.backward();
	        }
	
	        leftMotor.rotate(
	        	convertAngle(AbstractConfig.LEFT_RADIUS, AbstractConfig.WIDTH, degree)
	        	, true);
	        rightMotor.rotate(
	        	-convertAngle(AbstractConfig.RIGHT_RADIUS,AbstractConfig.WIDTH , degree)
	        	, returnRightAway);
//		}
	}
	
	/**
	 * 
	 * @param radius radius of the wheel 
	 * @param distance distance we want to cover 
	 * @return angle the wheel need to rotate in degree
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * convert angle in deg to the distance driven in cm
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	/**
	 * turn to angle wrt to the y axies 
	 */
	public void turnTo(double theata) {
		rotateToRelatively(theata);		
	}
	public static void setSpeed(int speed){
		AbstractConfig.LEFT_MOTOR.setSpeed(speed);
		AbstractConfig.RIGHT_MOTOR.setSpeed(speed);
	}
	
	public void motorForward(){
//		synchronized(lock){
			AbstractConfig.LEFT_MOTOR.forward();
			AbstractConfig.RIGHT_MOTOR.forward();			
//		}

	}
	public void motorStop(){
		AbstractConfig.LEFT_MOTOR.stop();
		AbstractConfig.RIGHT_MOTOR.stop();
	}
	
}