package lab3StartFromFresh;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.LCPBTResponder;

public class Configuration implements RobotConfiguration{

	private Coordinate start  = new Coordinate(0,0,0); // starting location
	private Odometer odometer;
	private LCDWriter monitor;
	private LCPBTResponder lcpThread;
	static boolean driveComplete  = false ;
	
	public static Configuration getNewDefaultConfiguration(){
		return new Configuration();
	}
	
	public Configuration(){
		odometer = new Odometer(this);
		monitor = new LCDWriter(this);
		lcpThread = new LCPBTResponder();
		lcpThread.setDaemon(true);
		lcpThread.start();
	}
	@Override
	public double getAvgRadius() {
		return (LEFT_RADIUS + RIGHT_RADIUS)/2;
	}
	@Override
	public Odometer getOdometer() {
		return odometer;
	}
	@Override
	public void setOdometer(Odometer odometer) {
		this.odometer = odometer;
	}
	@Override
	/**
	 * get all the moving motors, 
	 * right now we only use the left and right motor
	 */
	public NXTRegulatedMotor [] getAllMotors() {
		NXTRegulatedMotor motorSets [] = new NXTRegulatedMotor[2] ;
		motorSets[0] = LEFT_MOTOR;
		motorSets[1] = RIGHT_MOTOR;
		return motorSets;
	}
	@Override
	public LCDWriter getMonitor() {
		return monitor;
	}
	@Override
	public void setMonitor(LCDWriter monitor) {
		this.monitor = monitor; 
	}
	public double getWidth() {
		return WIDTH;
	}

	@Override
	public Coordinate getStartingCoordinate() {
		// TODO Auto-generated method stub
		return start;
	}

	public void setStartingCoordinate(Coordinate c ) {
		start = c;
	}

	@Override
	public void writeToMonitor(String str,int ln) {
		this.getMonitor().writeToScreen(str, ln);

	}

	@Override
	public boolean driveComplete() {
		return 	driveComplete;
	}
	
	public void setDriveComplete(boolean comp) {
		driveComplete = comp;
	}
}
