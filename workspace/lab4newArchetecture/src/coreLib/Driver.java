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
	private NXTRegulatedMotor leftMotor= AbstractConfig.LEFT_MOTOR , 
								rightMotor = AbstractConfig.RIGHT_MOTOR;

	private static Driver instance ;
	private Object lock ;
	private Odometer odo = Odometer.getInstance();
	/**
	 * indicate if the motor is running or not.
	 * set by motorStop and motorForward
	 */
	private boolean motorStopped = true ;
	private boolean termianteActions = false;
	
	/**
	 * do not use this to initialize another instance. only used for extension 
	 * @param config
	 */
	@Deprecated
	public Driver(AbstractConfig config){
		this.config = config ;


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
	 * @param nextLocationg
	 */
	public void travelTo(Coordinate nextLocation) {
		Coordinate currentLoc  = new Coordinate(odo.getX(), odo.getY(), odo.getTheta());
		config.setNextLocation(nextLocation);
		config.setStartLocation(currentLoc.clone());
		
		double distance = Coordinate.calculateDistance(currentLoc, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentLoc, nextLocation);
		
		RConsole.println("Driver:travelTo:CurrentCoord: " + currentLoc.toString2());
		RConsole.println("Driver:travelTo:NxtCoord: " + nextLocation.toString2());
		RConsole.println("Driver:travelTo:traveling dist: " + distance);
		RConsole.println("Driver:travelTo:turning Angle: " + turningAngle);
		//make turn
		rotateToRelatively(turningAngle);

		setSpeed(config.getForwardSpeed());
		
		forward(distance);
		
		RConsole.println("Driver:travelTo:currentCoordinate : x " + config.getCurrentLocation().getX()
			+"\ty " + config.getCurrentLocation().getY() 
			+ "\ttheata " +config.getCurrentLocation().getTheta());
			RConsole.println("=======");
		
			
		}
		
	/**
	 * move wheel forward at the same speed it was running at before 
	 * @param dist
	 */
	public void forward(double dist){
		/*
		 * this should not be used since it's impossible to stop  the motor 
		 * leftMotor.rotate(
				convertDistance(AbstractConfig.LEFT_RADIUS, dist), 
				true
				);
			rightMotor.rotate(
				convertDistance(AbstractConfig.RIGHT_RADIUS, dist), 
				false
				);
	*/
		
//		here is better 
		int finalTachoCount = (int) (AbstractConfig.LEFT_MOTOR.getTachoCount() + 
				(2*Math.PI*	(AbstractConfig.LEFT_RADIUS + AbstractConfig.RIGHT_RADIUS)/2
				)*360) ;
		
		motorForward();
		while(!motorStopped && finalTachoCount- leftMotor.getTachoCount() > 0 ){
			try{Thread.sleep(20);} catch (Exception e){};
		}
		motorStop();
	}
	
	/**
	 * move wheel backward at the same speed it was running at before 
	 * @param dist
	 */
	public void backward(double dist){
		int finalTachoCount = (int) (AbstractConfig.LEFT_MOTOR.getTachoCount() + 
				(2*Math.PI*	(AbstractConfig.LEFT_RADIUS + AbstractConfig.RIGHT_RADIUS)/2
				)*360) ;
		
		motorBackward();
		while(!motorStopped && finalTachoCount- leftMotor.getTachoCount() > 0 ){
			try{Thread.sleep(20);} catch (Exception e){};
		}
		motorStop();
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
	        if (degree < 0){		//if degree is negative then rotate back ward
	        	motorBackward();
	        }
	        leftMotor.rotate(
	        	convertAngle(AbstractConfig.LEFT_RADIUS, AbstractConfig.WIDTH, degree)
	        	, true);
	        rightMotor.rotate(
	        	-convertAngle(AbstractConfig.RIGHT_RADIUS,AbstractConfig.WIDTH , degree)
	        	, returnRightAway);
	        
	        motorStop();
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
	 * @param in degrees 
	 */
	public void turnTo(double theata) {
		rotateToRelatively(theata);		
	}
	/**
	 * set wheel rotation speed in deg/sec 
	 * @param speed
	 */
	public static void setSpeed(int speed){
		AbstractConfig.LEFT_MOTOR.setSpeed(speed);
		AbstractConfig.RIGHT_MOTOR.setSpeed(speed);
	}
	/**
	 * set motor to go forward till stop()
	 */
	public void motorForward(){
		motorStopped = false ;
		AbstractConfig.LEFT_MOTOR.forward();
		AbstractConfig.RIGHT_MOTOR.forward();		

	}
	/**
	 * set motor to go backward till stop()
	 */
	public void motorBackward(){
		motorStopped = false ;
		AbstractConfig.LEFT_MOTOR.backward();
		AbstractConfig.RIGHT_MOTOR.backward();
	}
	public void motorStop(){
		motorStopped = true ;
		AbstractConfig.LEFT_MOTOR.stop();
		AbstractConfig.RIGHT_MOTOR.stop();
	}

}
