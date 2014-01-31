package lab3;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;



/**
 * makes the drive and contians the main method of this program 
 * @author yuechuan
 *
 */
public class Driver implements Lab3, RobotConfigration{
	
	private static int buttonChoice;
	public static final double WIDTH_OF_SQUARE = 30.48; 
	private static Driver d; // this is the singleton of this class 
	
	private NXTRegulatedMotor leftMotor;
	private NXTRegulatedMotor rightMotor;
	private double leftRadius, rightRadius, width;	
	//this zigzag does the driving , see more at ZigZag class
	private Drivable drive ;
	private Odometer odometer;

	public Driver(Drivable drive,NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, double leftRadius , 
			double rightRadius , double width){
		
		//set up odo
		odometer = new Odometer();
		odometer.setMotors(leftMotor ,rightMotor)
				.setRadius(leftRadius, rightRadius)
				.setSeparation(width);
		
		//set up correction
//		OdometryCorrection odometryCorrection = new OdometryCorrection(odometer,colorSensor);
		//set up display
//		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer, odometryCorrection);
		
		//set up motor and wheels		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = leftRadius;
		this.rightRadius = rightRadius;
		this.width = width;
		
		//set up zigzag
		this.drive.setConfiguration(this);
		
	}
	
	public static void main (){
		
		Drivable zigzag = new ZigZag();
		
		d = new Driver(zigzag,Motor.A, Motor.B, 
				2.09, 2.09 , 15.24);	//radius and width 
		

		
		do {
			
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Float | Drive  ", 0, 2);
			LCD.drawString("motors | zigZag", 0, 3);
			LCD.drawString("       | ......", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {
			for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B, Motor.C }) {
				motor.forward();
				motor.flt();
			}

			// start only the odometer and the odometry display
			d.startOdometer();;
			//TODO add odoDisplay
		}
		else {
			// start the odometer
			d.startOdometer();
			//TODO add odoDisplay

			// spawn a new Thread to avoid SquareDriver.drive() from blocking
			(new Thread() {
				public void run() {
					d.drive();
				}
			}).start();
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
		
		

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
	public Odometer getOdometer() {
		return odometer;
	}
	
	public void startOdometer(){
		odometer.start();
	}
	
	/**
	 * start to drive the robot. 
	 * in this case it should run the zigZag 
	 */
	public void drive() {
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

	public NXTRegulatedMotor getLeftMotor() {
		return leftMotor;
	}

	public void setLeftMotor(NXTRegulatedMotor leftMotor) {
		this.leftMotor = leftMotor;
	}

	public NXTRegulatedMotor getRightMotor() {
		return rightMotor;
	}

	public void setRightMotor(NXTRegulatedMotor rightMotor) {
		this.rightMotor = rightMotor;
	}

	public double getLeftRadius() {
		return leftRadius;
	}

	public void setLeftRadius(double leftRadius) {
		this.leftRadius = leftRadius;
	}

	public double getRightRadius() {
		return rightRadius;
	}

	public void setRightRadius(double rightRadius) {
		this.rightRadius = rightRadius;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public Drivable getDrive() {
		return drive;
	}

	public void setDrive(Drivable drive) {
		this.drive = drive;
	}
	

}
