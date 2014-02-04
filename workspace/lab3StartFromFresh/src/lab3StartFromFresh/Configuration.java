package lab3StartFromFresh;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.LCPBTResponder;

public class Configuration implements RobotConfiguration{

	private Object lock ;
	private Coordinate start  = new Coordinate(0,0,0); // starting location
	private Coordinate currentLocation = start.clone(); //current location ;
	private Coordinate nextLocation; //current location ;
	private Odometer odometer;
	private LCDWriter monitor;
	private Planner planner ;
//	private LCPBTResponder lcpThread;   /pc debugging
	private Driver driver ;
	static boolean driveComplete  = false ;
	
	public static Configuration getNewDefaultConfiguration(){
		Configuration config = new Configuration();
		config.odometer = new Odometer(config);
		config.monitor = new LCDWriter(config);
		config.planner = new UltraSonicPlanner(config);
		return config;
	}
	
	public Configuration(){
		lock = new Object();
//		lcpThread = new LCPBTResponder();
//		lcpThread.setDaemon(true);
//		lcpThread.start();
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
		Coordinate coord ;
		synchronized(lock){
			coord = start;
		}
		return coord;
	}

	public void setStartingCoordinate(Coordinate c ) {
		synchronized (lock) {
			start = c;
		}
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

	@Override
	public Coordinate getCurrentLocation() {
		Coordinate coord ;
		synchronized(lock){
			coord = currentLocation;
		}
		return coord;
	}

	@Override
	public void setCurrentLocation(Coordinate loc) {
		synchronized(lock){
			currentLocation = loc;
		}
	}

	@Override
	public Coordinate getNextLocation() {
		return nextLocation;
	}

	@Override
	public void setNextLocation(Coordinate loc) {
		nextLocation = loc;
	}

	@Override
	public RobotConfiguration setDriver(Driver driver) {
		this.driver = driver;
		return this;
	}

	@Override
	public Driver getDriver() {

		return driver;
	}

	@Override
	public Planner getPlanner() {
		return planner;
	}

	@Override
	public void setPlanner(Planner p) {
		planner = p;
	}
	
	public void leftButtonPressed(){
		//really important ... don't forget to start 
		monitor.start();
		odometer.start();
		planner.start();//ultrasonic poller and pCOntrol are started implicitly by planner 
		driver.start();
		
	}
	@Override
	/**
	 * reset the motot speed to default forward_speed and motor rotation to forward
	 */
	public void resetMotorSpeed() {
		RobotConfiguration.LEFT_MOTOR.stop();
		RobotConfiguration.RIGHT_MOTOR.stop();
		
		RobotConfiguration.LEFT_MOTOR.setSpeed(FORWARD_SPEED);
		RobotConfiguration.RIGHT_MOTOR.setSpeed(FORWARD_SPEED);
		
		RobotConfiguration.LEFT_MOTOR.forward();
		RobotConfiguration.RIGHT_MOTOR.forward();
	}

	@Override
	public void rightButtonPressed() {
		// TODO Auto-generated method stub
		
	}
}
