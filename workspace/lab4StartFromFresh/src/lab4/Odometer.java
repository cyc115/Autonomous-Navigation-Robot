package lab4;

import lejos.nxt.NXTRegulatedMotor;

public class Odometer extends Thread {
	// odometer update period, in ms
	private static final long UPDATE_PERIOD = 25; //ms
	private Object lock; 	// lock object for mutual exclusion

	private RobotConfiguration config;
	private Coordinate cCoord ;  //current location
	private NXTRegulatedMotor lMotor; // left motor 
	private NXTRegulatedMotor rMotor;
	private int lTCount, rTCount;	// the previous tacho meter counts of left and right motor

	Odometer(RobotConfiguration config){
		this.config = config;
		lock = new Object();
		lMotor = RobotConfiguration.LEFT_MOTOR;
		rMotor = RobotConfiguration.RIGHT_MOTOR;
		
		cCoord = config.getCurrentLocation();
	}
	
	/**
	 * Calculate the arc length of movement (in degree) taking into the account of the wheel radius 
	 * @param deltaTachometerCount tacho count in degree
	 * @return distance in cm 
	 */
	private double getArcLen(int deltaTachometerCount,double radius) {
		return Math.toRadians(deltaTachometerCount) * radius;
	}
	public void run(){
		long updateStart, updateEnd;
		double leftArcDistance;
		double rightArcDistance;
		double deltaTheta;
		double displacement;
		double currentTheta;
		double deltaX;
		double deltaY;
		
		while (true) {
			updateStart = System.currentTimeMillis();


				updateStart = System.currentTimeMillis();
				//get the starting tacho count
				int newLeftCount = lMotor.getTachoCount();
				int newRightCount = rMotor.getTachoCount();
				
				//calc difference in TCount from previous position 
				int deltaLeftCount = newLeftCount - lTCount;
				int deltaRightCount = newRightCount - rTCount;
				
				lTCount = newLeftCount;
				rTCount = newRightCount;
				
				//calculate the distance traveled for each wheel 
				leftArcDistance = getArcLen(deltaLeftCount,RobotConfiguration.LEFT_RADIUS);
				rightArcDistance = getArcLen(deltaRightCount,RobotConfiguration.RIGHT_RADIUS);
				

				//calculate the degree 
				deltaTheta = (leftArcDistance - rightArcDistance) / RobotConfiguration.WIDTH;
				//calculate the displacement 
				displacement = (leftArcDistance + rightArcDistance) / 2.0;
				
				currentTheta = getTheta();
				//computes the dist made on the x and y axes since last check 
				deltaX = displacement * Math.cos(currentTheta + deltaTheta / 2);
				deltaY = displacement * Math.sin(currentTheta + deltaTheta / 2);
		
				synchronized (lock) {			
					//update location (x.y)
					setX(getX() + deltaX);
					setY(getY() + deltaY);
					setTheta(currentTheta + deltaTheta);
				}
			
			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < UPDATE_PERIOD) {
				try {
					Thread.sleep(UPDATE_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {		}
			}
		}
	}
	

	/*
	 * =============================================================================
	 * ACCESSORS  and SETTERS
	 * =============================================================================
	 */
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = cCoord.getX();
			if (update[1])
				position[1] = cCoord.getY();
			if (update[2])
				position[2] = cCoord.getTheta(); 
		}
	}
	
	public double getY() {
		double result;

		synchronized (lock) {
			result = cCoord.getX();
		}

		return result;
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = cCoord.getY();
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = cCoord.getTheta();
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				cCoord.setX(position[0]);
			if (update[1])
				cCoord.setY(position[1]);
			if (update[2])
				cCoord.setTheta(position[2]);
		}
	}

	
	public double getThetaInDeg(){
		return getTheta() * 57.2957795131;
	}
	
	/*
	 * =============================================================================
	 * END OF ACCESSORS and SETTERS
	 * =============================================================================
	 */
	/**
	 * returns the average tachoCount of both motors 
	 * @return the average tacho count 
	 */
	public int getTachoCount(){
		return ((lMotor.getTachoCount() + rMotor.getTachoCount() )/2) ;
	}
}
