/*
 * Lab2.java
 */
import lejos.nxt.*;

public class Lab2 {
	private static ColorSensor colorSensor = new ColorSensor(SensorPort.S2); //light sensor at port2
		
	private static NXTRegulatedMotor LEFT_MOTOR = Motor.A, RIGHT_MOTOR = Motor.B;
	public static final double LEFT_RADIUS = 2.09,RIGHT_RADIUS =2.0900 ;
	public static final double SEPARATION = 15.24 ;
	public static final double WIDTH_OF_SQUARE = 30.48; //cm
	public static final Coordinate ORIGIN = new Coordinate(15.24, 15.24);
	public static final Coordinate TOP_CORNER = new Coordinate(30.48, 30.48);
	
	private static int buttonChoice;
	
	
	public static double getAvgRadius() {
		return ( LEFT_RADIUS + RIGHT_RADIUS) / 2;
	}


	public static void main(String[] args) {
		//set up 
		Odometer odometer = new Odometer();	
		OdometryCorrection odometryCorrection = new OdometryCorrection(odometer,colorSensor);
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer, odometryCorrection);
		odometer.setMotors(LEFT_MOTOR ,RIGHT_MOTOR).setRadius(LEFT_RADIUS, RIGHT_RADIUS).setSeparation(SEPARATION);

		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Float | Drive  ", 0, 2);
			LCD.drawString("motors | in a   ", 0, 3);
			LCD.drawString("       | square ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {
			for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B, Motor.C }) {
				motor.forward();
				motor.flt();
			}

			// start only the odometer and the odometry display
			odometer.start();
			odometryDisplay.start();
		} else {
			// start the odometer, the odometry display and (possibly) the
			// odometry correction
			odometer.start();
			odometryDisplay.start();
			odometryCorrection.start();

			// spawn a new Thread to avoid SquareDriver.drive() from blocking
			(new Thread() {
				public void run() {
					SquareDriver.drive(Motor.A, Motor.B, 2.09, 2.0995, 15.24);
				}
			}).start();
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}