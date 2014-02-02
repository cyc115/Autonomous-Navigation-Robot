package lab3StartFromFresh;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public interface RobotConfiguration {
	public int FORWARD_SPEED = 200;
	public int ROTATE_SPEED = 150;
	NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	static double LEFT_RADIUS = 2.09 ;
	static double RIGHT_RADIUS =2.0900;
	
	public Coordinate getStartingCoordinate ();
	public void setStartingCoordinate (Coordinate c);
	public double getAvgRadius();
	public Odometer getOdometer();
	public void setOdometer(Odometer odometer);
	/**
	 * get all the moving motors 
	 * @return
	 */
	public NXTRegulatedMotor [] getAllMotors();
	
	//TODO get/set Correction;
	
	public LCDWriter getMonitor ();
	public void setMonitor(LCDWriter monitor);
	public void writeToMonitor(String str, int ln);
	public double getWidth();
	
}
