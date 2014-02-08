package lab4;

import lejos.nxt.ColorSensor;
import lejos.nxt.LightSensor;

public class LineReader extends Thread{
	ColorSensor colorSensor;
	private RobotConfiguration config;
	private int previousSensedValue = 46, currentSensedValue = 46;
	private boolean passedLine = false ;
	

	LineReader(RobotConfiguration config){
		colorSensor  = new ColorSensor(RobotConfiguration.LIGHT_SENSOR_PORT);

		this.config = config;
	}
	public void run (){
		colorSensor.setFloodlight(true);
		
		//finish loop when run is done 
		while(!config.driveComplete()){
			
			previousSensedValue = currentSensedValue;
			currentSensedValue = colorSensor.getLightValue();
			
			if (hasPassedLine(currentSensedValue, previousSensedValue)){
				passedLine = true ;
				try {Thread.sleep(200);	} catch (InterruptedException e) {		}
			}
			else {
				passedLine = false ;
			}
		}
		
		//shuts off light to save the earth
		colorSensor.setFloodlight(false);
	}
	
	public int getLightValue(){
		return currentSensedValue;
	}
	public boolean isPassedLine() {
		return passedLine;
	}
	/**
	 * derivative if robot has passed line 
	 * @param currentSensedValue
	 * @param previousSensedValue
	 * @return a determination if one has passed a line or not
	 */
	private boolean hasPassedLine(int currentSensedValue, int previousSensedValue) {
		//INITIALIZE THE LAST DETECTION TIME TO AVOID false positive
		// at the beginning of the robot movement 
		long lastDetectionTime =2500; 
		int lightSensorThreshold = 7 ;// how sensitive sensor should be when it detects changes
		int ignorePeriod = 500 ; //time in ms to ignore further input 
		boolean hasDetected = ((previousSensedValue - currentSensedValue ) > lightSensorThreshold) ;
		boolean result;
		long diffInDectectionTime ; // time between positive feedbacks from sensor 
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
	
}
