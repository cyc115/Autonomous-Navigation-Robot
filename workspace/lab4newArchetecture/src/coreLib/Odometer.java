package coreLib;

import lejos.nxt.NXTRegulatedMotor;

public class Odometer extends Thread {
	private static boolean threadStarted = false ;
	private static Odometer odo;
	
	// odometer update period, in ms
	private static final long UPDATE_PERIOD = 25; //ms
	private Object lock; 	// lock object for mutual exclusion

	private AbstractConfig config;
	private Coordinate cCoord ;  //current location (x,y,heading)
	private NXTRegulatedMotor lMotor; // left motor 
	private NXTRegulatedMotor rMotor;
	private int lTCount, rTCount;	// the previous tacho meter counts of left and right motor

	/**
	*returns an instance of odometer and run it 
	**/
	public static Odometer getInstance(){
		if (odo == null){
			odo = new Odometer(AbstractConfig.getInstance());
		}
		return odo;
	}

	private Odometer(AbstractConfig config){
		this.config = config;
		lock = new Object();
		lMotor = Configuration.LEFT_MOTOR;
		rMotor = Configuration.RIGHT_MOTOR;
		
		cCoord = config.getCurrentLocation();
	}


	public void run(){
		threadStarted = true;
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
				leftArcDistance = getArcLen(deltaLeftCount,AbstractConfig.LEFT_RADIUS);
				rightArcDistance = getArcLen(deltaRightCount,AbstractConfig.RIGHT_RADIUS);
				

				//calculate the degree 
				deltaTheta = (leftArcDistance - rightArcDistance) / AbstractConfig.WIDTH;
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
				} catch (InterruptedException e) {	}
			}
		}
	}


	/*
	 * =============================================================================
	 * ACCESSORS  and SETTERS
	 * =============================================================================
	 */

	/**
	 * Calculate the arc length of movement (in degree) taking into the account of the wheel radius 
	 * @param deltaTachometerCount tacho count in degree
	 * @return distance in cm 
	 */
	private double getArcLen(int deltaTachometerCount,double radius) {
		return Math.toRadians(deltaTachometerCount) * radius;
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
	
	private Coordinate setY(double x) {
		synchronized (lock) {
			cCoord.setX(x);
		}
		return cCoord;
	}

	private Coordinate setX(double y) {
		synchronized (lock) {
			cCoord.setY(y);
		}
		return cCoord;
	}

	private Coordinate setTheta(double theta) {
		synchronized (lock) {
			cCoord.setTheta(theta);
		}
		return cCoord;
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
