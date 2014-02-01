package lab3StartFromFresh;

public class Odometer extends Thread {
	// odometer update period, in ms
	private static final long UPDATE_PERIOD = 25; //ms
	private Object lock; 	// lock object for mutual exclusion
	
	private double x, y, theta; //theta is the facing angle in rad wrt it's beginning face direction
	private RobotConfiguration config;
	
	Odometer(RobotConfiguration config){
		this.config = config;
		
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
	public void run(){
		long updateStart, updateEnd;
		double leftArcDistance;
		double rightArcDistance;
		double deltaTheta;
		double displacement;
		double currentTheta;
		double deltaX;
		double deltaY;
	}
}
