package lab3;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;

/* 
 * OdometryCorrection.java
 */

public class OdometryCorrection extends Thread {
	private Object lock ;
	
	private static final long CORRECTION_PERIOD = 10;
	private int currentLightValue; //the current light detected by the sensor
	private int normalizedHight = -1, normalizedLow = -1;
	private int lineNumber = 0;		//count the number of square passed 
	private int previousSensedValue, currentSensedValue = 55;
	private long lastDetectionTime;  //the time of last6cx detection
	private long diffInDectectionTime ; // time between positive feedbacks from sensor 
	private double defaultSqrLen = 30.48; //the width of the square
	
	//final position of the robot wrt to the TOP_CORNER
	private Coordinate finalPosition = new Coordinate();
	private Odometer odometer;	//odo 
	private RobotConfigration config;
	private Drivable drive ;
	private ColorSensor colorSensor ; //color sensor
	
	//following var is used to calculate robot angle wrt t
	int beginTachoCount = -1 , endTachoCount = -1 ; //temp variables used to calc dist covered per sqr
	double distXCovered =-1 , distYCovered = -1; //distance X, y covered in the first sqr

	public OdometryCorrection(Odometer odometer,ColorSensor lightSensor,RobotConfigration config) {
		this.odometer = odometer;
		this.colorSensor = lightSensor;
		this.config = config;
		drive = config.getDriver();
	}
	
	public void run() {
		long correctionStart, correctionEnd;
		colorSensor.setFloodlight(true); 	//set color on 
		//INITIALIZE THE LAST DETECTION TIME TO AVOID false positive at the beginning of the robot movement 
		lastDetectionTime =2000; 
		
		double robotOffset = 12.5; //length of the robot
		
		while (true) {
			//set up 
				correctionStart = System.currentTimeMillis();
				previousSensedValue = currentSensedValue;
				currentSensedValue = colorSensor.getLightValue();
				
				//count number of lines
				/*
				
				if (hasPassedLine(currentSensedValue,previousSensedValue)){
					lineNumber++;
					if (SquareDriver.getCurrentMovementDirection() == SquareDriver.MOVEMENT_UP ){
						odometer.setX(lineNumber * defaultSqrLen + robotOffset);
					}
					else if (SquareDriver.getCurrentMovementDirection() == SquareDriver.MOVEMENT_RIGHT ){
						odometer.setY((lineNumber - 2 ) * defaultSqrLen + robotOffset);
					}
					else if (SquareDriver.getCurrentMovementDirection() == SquareDriver.MOVEMENT_DOWN ){
						odometer.setX( ((7-lineNumber ) * defaultSqrLen) - robotOffset) ; //7 is the line where x = 0
					}

					else if (SquareDriver.getCurrentMovementDirection() == SquareDriver.MOVEMENT_LEFT ){
						odometer.setY(((9-lineNumber) * defaultSqrLen) - robotOffset);
					}
					
				}
				 */
				
				
				
				/**
				 * measure the x distance covered from the first line. 
				 * to calculate the angle of the robot wrt x axies 
				 */
				if (lineNumber == 0 && (beginTachoCount == -1 )){
					beginTachoCount = odometer.getTachoCount() ;
				}
				else if (lineNumber == 1 && (endTachoCount == -1 )){
					endTachoCount = odometer.getTachoCount() ;
					//calculate actual covered dist on the X axis in the first sqr
					distXCovered = 2* Math.PI * config.getAvgRadius() * (endTachoCount - beginTachoCount) /360;
				}
				//reset the variables for the next two else if cases 
				else if (lineNumber == 2 && beginTachoCount != -1  ){
					//reset beg and end Tacho count
					beginTachoCount = -1 ;
					endTachoCount = -1 ;
				}
				
				/**
				 * Measure the y distance covered.
				 */
				else if (lineNumber == 8 && (beginTachoCount == -1 )&& !drive.isCompleted()){
					beginTachoCount = odometer.getTachoCount();
				}
				else if (lineNumber == 8 && (endTachoCount == -1) && drive.isCompleted()){
					endTachoCount = odometer.getTachoCount();
					//calculate actual covered dist on the Y axis in the first sqr
					distYCovered = 2* Math.PI * config.getAvgRadius() * (endTachoCount - beginTachoCount) / 360 ;
				
					//determin the final position 
					
					//original is this 
//					finalPosition.setX(Lab2.TOP_CORNER.getX() - distXCovered);
//					finalPosition.setY(Lab2.TOP_CORNER.getY() - distYCovered);
					
					finalPosition.setX(100 - distXCovered); //TODO this 100 is jst a bogus value
					finalPosition.setY(100 - distYCovered);
					break;
				}		
		
			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {

				}
			}
		}
	}
	
//	public String finalDistToString(){
//		return distBtwnFinalAndOrigin.toString();
//	}
	
	/**
	 * derivative if robot has passed line 
	 * @param currentSensedValue
	 * @param previousSensedValue
	 * @return a determination if one has passed a line or not
	 */
	private boolean hasPassedLine(int currentSensedValue, int previousSensedValue) {
		int lightSensorThreshold = 2 ;// how sensitive sensor should be when it detects changes
		int ignorePeriod = 5000 ; //time in ms to ignore further input 
		boolean hasDetected = ((previousSensedValue - currentSensedValue ) > lightSensorThreshold) ;
		boolean result;
		
		//avoid detecting a line twice : two valid detection has to be 800 ms appart 
		diffInDectectionTime = (System.currentTimeMillis() - lastDetectionTime);
		
		//if this is a new line and should be counted
		if (hasDetected && (diffInDectectionTime > ignorePeriod)){
			lastDetectionTime = System.currentTimeMillis();
			result = hasDetected;
		}
		//if this is a false positive
		else if (hasDetected){
			lastDetectionTime = System.currentTimeMillis();
			result = false;
		}
		//if negative 
		else{
			result = false ;
		}
		//reset the time between detection .
		diffInDectectionTime = 0 ;
		return result ; 
	}
	
	/**
	 * Getters and Setters  
	 **/

	public long getLastDetectionTime() {
			return lastDetectionTime;
	}
	public long getDiffInDectectionTime() {
			return diffInDectectionTime;
	}

	public int getNormalizedHight() {
			return normalizedHight;
	}

	public int getNormalizedLow() {
			return normalizedLow;
	}

	public int getCurrentLightValue (){
			return currentSensedValue;
	}
	public int getNormalizedLightValue (){
			return colorSensor.getNormalizedLightValue();
	}
	
	/**
	 * get the number of square
	 * @return
	 */
	public int getLineNumber(){
			return lineNumber;
	}
	/**
	 * override the line number 
	 */
	public void setLineNumber(int line){
		lineNumber = line;
	}
	
	public int getBeginTachoCount() {
		return beginTachoCount;
	}

	public int getEndTachoCount() {
		return endTachoCount;
	}


}