package lab4newArchetecture;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

public abstract class Driver extends Thread{
	private AbstractConfig config ;
	private Coordinate currentCoordinate, startCoord , endCoord;
	private NXTRegulatedMotor leftMotor , rightMotor;

	public Driver(AbstractConfig config){
		this.config = config ;
		leftMotor = AbstractConfig.LEFT_MOTOR;
		rightMotor = AbstractConfig.RIGHT_MOTOR;
		
		currentCoordinate = config.getCurrentLocation();
		endCoord = config.getStartLocation();
	}

	public void travelTo(int x, int y){
		travelTo(new Coordinate(x, y, 0));
	}
	
	/**
	 * travel to wrt to the global (0,0) coordinate
	 * @param nextLocationg
	 */
	public void travelTo(Coordinate nextLocation) {
		config.setNextLocation(nextLocation);
		config.setCurrentLocation(currentCoordinate.clone());

		double distance = Coordinate.calculateDistance(currentCoordinate, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentCoordinate, nextLocation);

		RConsole.println("Driver:travelTo:NxtCoord: " + nextLocation.toString());
		RConsole.println("Driver:travelTo:traveling dist: " + distance);
		RConsole.println("Driver:travelTo:turning Angle: " + turningAngle);
		
		//make turn
		rotateToRelatively(turningAngle);
		//set to forward speed 
		RConsole.println("Driver:travelTo:setting speed: " + config.getForwardSpeed());
		AbstractConfig.LEFT_MOTOR.setSpeed(config.getForwardSpeed());
		AbstractConfig.RIGHT_MOTOR.setSpeed(config.getForwardSpeed());
		
		/* disabled obstacle avoidance during traveling 
		boolean finishedTravelTo = false ;
		
		while(!finishedTravelTo){
			
			//when navigating
			while(true){
				double moveDist;
				//move x cm forward if distance is bigger then 1cm
				if (distance > CHECK_DISTANCE ){
					moveDist = CHECK_DISTANCE;
					distance -= CHECK_DISTANCE;	
				}
				else {
					moveDist = distance ;
					finishedTravelTo = true ;
					break;
				}
				travel(moveDist);
			}
		}
		*/
		
		travel(distance);
		Coordinate temp = new Coordinate(
					nextLocation.getX(), nextLocation.getY() ,
					Coordinate.normalize((currentCoordinate.getTheta()
							+ turningAngle  //TODO i think this is where my angle is wrong , turningAngle is in degree and Theta is in rad
							))
				);
		currentCoordinate = temp ;
		RConsole.println("Driver:travelTo:currentCoordinate : x " + config.getCurrentLocation().getX()
				+"\ty " + config.getCurrentLocation().getY() 
				+ "\ttheata " +config.getCurrentLocation().getTheta());
	}
	
	/**
	 * move forward x cm 
	 * @param dist
	 */
	public void travel(double dist){
		
		rightMotor.setSpeed(config.getForwardSpeed());
		leftMotor.setSpeed(config.getForwardSpeed());
		
		leftMotor.rotate(
				convertDistance(AbstractConfig.LEFT_RADIUS, dist), 
				true
				);
		rightMotor.rotate(
				convertDistance(AbstractConfig.RIGHT_RADIUS, dist), 
				false
				);
	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 *	<br> this method is here only for comparability.
	 *	<br> use rotateToRelatively (doubel degree , boolean returnRightAway);
	 *	when ever possible 
	 * @param degree
	 */
	protected void rotateToRelatively(double degree){
		rotateToRelatively(degree, false);
	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 * @param degree 
	 * @param returnRightAway should the function finish before finishing the turn 
	 */
	public void rotateToRelatively(double degree, boolean returnRightAway){
		rightMotor.setSpeed(config.getRotationSpeed());
		leftMotor.setSpeed(config.getRotationSpeed());
		
		
		if (degree < 0){		//if degree is negative then rotate back ward
			leftMotor.backward();
			rightMotor.backward();
		}
		
		leftMotor.rotate(
				convertAngle(AbstractConfig.LEFT_RADIUS, config.WIDTH, degree)
				, true);
		rightMotor.rotate(
				-convertAngle(AbstractConfig.RIGHT_RADIUS,config.WIDTH , degree)
				, returnRightAway);
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
}
