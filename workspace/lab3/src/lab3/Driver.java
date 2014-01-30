package lab3;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;



/**
 * makes the drive and contians the main method of this program 
 * @author yuechuan
 *
 */
public class Driver implements Lab3{
	
	public static final double WIDTH_OF_SQUARE = 30.48; 
	
	private NXTRegulatedMotor leftMotor;
	private NXTRegulatedMotor rightMotor;
	private double leftRadius, rightRadius, width;	
	//this zigzag does the driving , see more at ZigZag class
	private Drivable drive ;
	

	public Driver(Drivable drive,NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, double leftRadius , 
			double rightRadius , double width){
		
		//set up this class 
		this.drive = drive;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = leftRadius;
		this.rightRadius = rightRadius;
		this.width = width;
		//set up zigzag
		drive.setLeftMotor(leftMotor);
		drive.setRightMotor(rightMotor);
		drive.setLeftRadius(leftRadius);
		drive.setRightRadius(rightRadius);
		drive.setWidth(width);
		
	}
	
	public static void main (){
		Drivable zigzag = new ZigZag();
		
		Driver d = new Driver(zigzag,Motor.A, Motor.B, 
				2.09, 2.09 , 15.24);
		
		d.drive();
//cm
		
		
		/*
		//angle in rad
		double firstAngle = Math.atan(Math.toRadians(30)/Math.toRadians(60));//first turning ang 
		double secondAngle =firstAngle - Math.PI  ;
		
//		driver.turnTo(firstAngle);
		driver.travelTo(60,30);
		
//		driver.turnTo(-secondAngle);
		driver.travelTo(30, 30);
		
//		driver.turnTo(Math.PI/2);
		driver.travelTo(30, 60);
		
//		driver.turnTo(Math.PI/2 + firstAngle);
		driver.travelTo(60, 0);
		
		*/
	}



	private void drive() {
		drive.start();
	}

	/**
	 * travel to the desired global location based on the odo data and 
	 */
	public void travelTo(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * turn the robot the given angle in rad , positive theata means turning
	 *  clock wise and negative means turning counter clock wise
	 * @theata in rad 
	 */
	public void turnTo(double theata) {
leftMotor.rotate(convertAngle(leftRadius, width, 90.0), true);
		
	}
	
	/**
	 * return true when the robot is navigating to a location.
	 * false if it is idle (not navigating) or can take navigating requests
	 */
	@Override
	public boolean isNagivating() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 
	 * @param radius radius of the wheel 
	 * @param distance distance we want to cover 
	 * @return 
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	

}
