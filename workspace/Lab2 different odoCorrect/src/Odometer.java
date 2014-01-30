import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

/*
 * Odometer.java
 */

public class Odometer extends Thread {
	// robot position
	private double x, y, theta; //theta is the facing angle in rad wrt it's beginning face direction
	private double leftRadius,rightRadius; //radius of wheel
	private double separation; //separation between wheel
	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;

	private NXTRegulatedMotor lMotor ; // left motor 
	private NXTRegulatedMotor rMotor ; // right motor 
	private int lTCount, rTCount;	// the previous tacho meter counts of left and right motor
	
	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
	}
	
	/**
	 * Calculate the arc length of movement (in degree) taking into the account of the wheel radius 
	 * @param deltaTachometerCount tacho count in degree
	 * @return distance in cm 
	 */
	private double getArcLen(int deltaTachometerCount,double radius) {
		return Math.toRadians(deltaTachometerCount) * radius;
	}
	
	// run method (required for Thread)
	public void run() {
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
			// put (some of) your odometer code here

			synchronized (lock) {
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
				leftArcDistance = getArcLen(deltaLeftCount,leftRadius);
				rightArcDistance = getArcLen(deltaRightCount,rightRadius);
				

				//calculate the degree 
				deltaTheta = (leftArcDistance - rightArcDistance) / separation;
				//calculate the displacement 
				displacement = (leftArcDistance + rightArcDistance) / 2.0;
				
				currentTheta = getTheta();
				//computes the dist made on the x and y axes since last check 
				deltaX = displacement * Math.cos(currentTheta + deltaTheta / 2);
				deltaY = displacement * Math.sin(currentTheta + deltaTheta / 2);
				
				//update location (x.y)
				setX(getX() + deltaX);
				setY(getY() + deltaY);
				setTheta(currentTheta + deltaTheta);

			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	public double degToRad(double degree){
		return degree/360 * 2 * Math.PI;
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
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}
	
	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
	
	/*
	 * =============================================================================
	 * END OF ACCESSORS and SETTERS
	 * =============================================================================
	 */
	
	
	/*
	 * =============================================================================
	 * THE FOLLOWING ARE BUILDER METHODS THAT MUST BE CALLED ON EACH ODOMETER OBJECT 
	 * =============================================================================
	 */
	/**
	 * set the left and right motor
	 * @param leftMotor
	 * @param rightMotor
	 * @return this odometry object
	 */
	public Odometer setMotors(NXTRegulatedMotor leftMotor,NXTRegulatedMotor rightMotor){
		lMotor = leftMotor;
		rMotor = rightMotor;
		return this;
	}
	/**
	 * set the radius of wheel.
	 * @param radius
	 * @return this Odometry object
	 */
	public Odometer setRadius(double lRadius, double rRadius){
		this.leftRadius = lRadius;
		this.rightRadius = rRadius;
		return this ;
	}
	
	public Odometer setSeparation (double separation){
		this.separation = separation;
		return this;
	}
	/**
	 * returns the average tachoCount of both motors 
	 * @return the average tacho count 
	 */
	public int getTachoCount(){
		return ((lMotor.getTachoCount() + rMotor.getTachoCount() )/2) ;
	}
	
	/*
	 * =============================================================================
	 * END OF BUILDER 
	 * =============================================================================
	 */
}