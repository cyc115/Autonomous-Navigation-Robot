package lab3;

import lejos.nxt.NXTRegulatedMotor;

public interface RobotConfigration {

	public NXTRegulatedMotor getLeftMotor();
	public void setLeftMotor(NXTRegulatedMotor leftMotor) ;
	public NXTRegulatedMotor getRightMotor();
	public void setRightMotor(NXTRegulatedMotor rightMotor);
	public double getLeftRadius() ;
	public void setLeftRadius(double leftRadius);
	public double getRightRadius() ;
	public void setRightRadius(double rightRadius);
	public double getWidth() ;
	public void setWidth(double width) ;
	public void setDriver(Drivable drive) ;
	public double getAvgRadius();
	/**
	 * returns the Drivable(interface) object that makes the actual drive of this robot
	 * @return
	 */
	public Drivable getDriver();
}
