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
	public Drivable getDrive();
	public void setDrive(Drivable drive) ;
}
