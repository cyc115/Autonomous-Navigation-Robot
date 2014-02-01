package lab3StartFromFresh;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Configuration implements RobotConfiguration{
	private double WIDTH = 15.24 ;
	private Odometer odometer;
	private LCDWriter monitor;
	
	public static Configuration getNewDefaultConfiguration(){
		return new Configuration();
	}
	
	public Configuration(){
		odometer = new Odometer(this);
		monitor = new LCDWriter(this);
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
}
