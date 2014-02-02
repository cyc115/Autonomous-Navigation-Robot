package lab3StartFromFresh;

import lejos.nxt.NXTRegulatedMotor;

public class ZigZag extends Thread implements Driver {
	
	private RobotConfiguration config ;
	private Coordinate currentCoordinate;
	private NXTRegulatedMotor leftMotor , rightMotor;

	
	ZigZag (RobotConfiguration config){
		this.config = config ;
		leftMotor = RobotConfiguration.LEFT_MOTOR;	// this is very frequently used
		rightMotor = RobotConfiguration.RIGHT_MOTOR;

		currentCoordinate = config.getStartingCoordinate();
	}
	
	
	public void run(){
		travelTo(new Coordinate(60,30,0)); //up 30 cm 
		travelTo(new Coordinate(30,30,0));
		travelTo(new Coordinate(30,60,0));
		travelTo(new Coordinate(60,0,0));
		//end of the run
		config.setDriveComplete(true);
	}

	/**
	 * travel to wrt to the global (0,0) coordinate
	 * @param nextLocation
	 */
	public void travelTo(double x, double y) {
		travelTo(new Coordinate(x,y,0));
	}

	/**
	 * travel to wrt to the global (0,0) coordinate
	 * @param nextLocation
	 */
	public void travelTo(Coordinate nextLocation){
		config.writeToMonitor(currentCoordinate.toString(), 3);
		config.writeToMonitor(nextLocation.toString(), 4);
		
		double distance = Coordinate.calculateDistance(currentCoordinate, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentCoordinate, nextLocation);
		
		config.writeToMonitor( ((Double)distance).toString(), 1);
		config.writeToMonitor( ((Double)turningAngle).toString(), 2);//TODO delete

		
		
		rotateToRelativly(turningAngle);
		
		leftMotor.rotate(
				convertDistance(RobotConfiguration.LEFT_RADIUS, distance), 
				true
				);
		rightMotor.rotate(
				convertDistance(RobotConfiguration.RIGHT_RADIUS, distance), 
				false
				);
		
		//TODO use odometry values in the future
		Coordinate temp = new Coordinate(
					nextLocation.getX(), nextLocation.getY() ,
					Coordinate.normalize((currentCoordinate.getTheta() + turningAngle))
				);
		currentCoordinate = temp ;
	}
	
	/**
	 * rotate to the angle wrt to the current robot angle 
	 * @param degree
	 */
	public void rotateToRelativly(double degree){
		
		rightMotor.setSpeed(RobotConfiguration.ROTATE_SPEED);
		leftMotor.setSpeed(RobotConfiguration.ROTATE_SPEED);
		
		if (degree < 0){		//if degree is negative then rotate back ward
			leftMotor.backward();
			rightMotor.backward();
		}
		leftMotor.rotate(
				convertAngle(RobotConfiguration.LEFT_RADIUS, config.getWidth(), degree)
				, true);
		rightMotor.rotate(
				-convertAngle(RobotConfiguration.RIGHT_RADIUS,config.getWidth() , degree)
				, false);
		
		rightMotor.setSpeed(RobotConfiguration.FORWARD_SPEED);
		leftMotor.setSpeed(RobotConfiguration.FORWARD_SPEED);
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
	 * turn to angle wrt to the y axies 
	 */
	@Override
	public void turnTo(double theata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNagivating() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public RobotConfiguration getConfig() {
		return config;
	}
	public void setConfig(RobotConfiguration config) {
		this.config = config;
	}
	public Coordinate getCurrentCoordinate() {
		return currentCoordinate;
	}
	public void setCurrentCoordinatel(Coordinate currentCoordinate) {
		this.currentCoordinate = currentCoordinate;
	}


}
