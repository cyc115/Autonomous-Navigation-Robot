package lab4newArchetecture;

import lejos.nxt.NXTRegulatedMotor;

public class Odometer {
	// odometer update period, in ms
	private static final long UPDATE_PERIOD = 25; //ms
	private Object lock; 	// lock object for mutual exclusion

	private AbstractConfig config;
	private Coordinate cCoord ;  //current location
	private NXTRegulatedMotor lMotor; // left motor 
	private NXTRegulatedMotor rMotor;
	private int lTCount, rTCount;	// the previous tacho meter counts of left and right motor

	Odometer(AbstractConfig config){
		this.config = config;
		lock = new Object();
		lMotor = Configuration.LEFT_MOTOR;
		rMotor = Configuration.RIGHT_MOTOR;
		
		cCoord = config.getCurrentLocation();
	}
}
