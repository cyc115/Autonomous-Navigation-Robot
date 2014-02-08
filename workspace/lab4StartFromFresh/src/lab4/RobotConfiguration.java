package lab4;

import lejos.nxt.I2CPort;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public interface RobotConfiguration {

	NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	public I2CPort ULTRASONIC_SENSOR_PORT = SensorPort.S1;
	public SensorPort LIGHT_SENSOR_PORT = SensorPort.S2;
	//over written in Configuration
	int ROTATE_SPEED = 120 ;
	int FORWARD_SPEED = 200;
	
	static double LEFT_RADIUS = 2.090 ;
	static double RIGHT_RADIUS =2.090;
	/**
	 * width between each wheels 
	 */
	static double WIDTH = 15.24 ;
	
	public LineReader getLineReader();
	public void setLineReader(LineReader lineReader);
	
	/**
	 * @return the starting coordinate of the run, should be defined implicitly by the Configuration 
	 * factory method 
	 */
	public Coordinate getStartingCoordinate ();
	/**
	 * set start location
	 * @param c
	 */
	public void setStartingCoordinate (Coordinate c);
	
	/**
	 * 
	 * @return average radius of the wheels of robot 
	 */
	public double getAvgRadius();
	/**
	 * 
	 * @return odometer used by the robot 
	 */
	public Odometer getOdometer();
	
	/**
	 * set the odometer used by the robot 
	 * @param odometer 
	 */
	public void setOdometer(Odometer odometer);
	/**
	 * get the LCD/pc monitor used for displaying information
	 * @return
	 */
	public LCDWriter getMonitor ();
	/**
	 * set the LCD/PC monitor used for displaying information
	 * @param monitor
	 */
	public void setMonitor(LCDWriter monitor);
	/**
	 * write string to line number , first line starts from 0 
	 * @param str
	 * @param ln
	 */
	public void writeToMonitor(String str, int ln);
	/**
	 * get the width of the robot 
	 * @return
	 */
	public double getWidth();
	/**
	 * 
	 * @return true when Driver has completed its run 
	 */
	public boolean driveComplete();
	/**
	 * Set the DriveComplete flag to signal the Driver has completed
	 * its run
	 * @param comp
	 */
	public void setDriveComplete(boolean comp);
	/**
	 * returns a reference to current Coordinate location
	 * @return
	 */
	public Coordinate getCurrentLocation();
	/**
	 * set the currentLocation to a Coordinate 
	 * @param loc
	 */
	public void setCurrentLocation(Coordinate loc);
	/**
	 * get the next Location that Driver want to 
	 * travel to 
	 * @return
	 */
	public Coordinate getNextLocation();
	/**
	 * set the next Location that Driver want to 
	 * travel to 
	 * @return
	 */
	public void setNextLocation(Coordinate loc);
	
	public RobotConfiguration setDriver(Drivable driver);
	public Drivable getDriver();
	
	public Planner getPlanner();
	public void setPlanner(Planner p);
	/**
	 * change the motor speed to this configuration's default 
	 */
	public void resetMotorSpeed();
	public void stopMotor();
	/**
	 * this will be called when leftButton is pressed on the brick
	 */
	public void leftButtonPressed();
	/**
	 * this will be called when rightButton is pressed on the brick
	 */
	public void rightButtonPressed();
	
	
	public UsPoller getUsPoller();

	public void setUsPoller(UsPoller ultrasonicPoller);
	
}
