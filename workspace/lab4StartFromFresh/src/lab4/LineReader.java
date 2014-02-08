package lab4;

import lejos.nxt.ColorSensor;
import lejos.nxt.LightSensor;

public class LineReader extends Thread{
	ColorSensor colorSensor;
	private RobotConfiguration config;
	private int previousSensedValue , currentSensedValue ;
	private boolean passedLine = false ;
	private long sensorStartTime;
	

	LineReader(RobotConfiguration config){
		colorSensor  = new ColorSensor(RobotConfiguration.LIGHT_SENSOR_PORT);

		this.config = config;
	}
	public void run (){
		colorSensor.setFloodlight(true);
		
		previousSensedValue = currentSensedValue = colorSensor.getLightValue();
		sensorStartTime = System.currentTimeMillis(); //mark the start time 
		
		//finish loop when run is done 
		while(!config.driveComplete()){
			
			previousSensedValue = currentSensedValue;
			currentSensedValue = colorSensor.getLightValue();
			
			if (hasPassedLine(currentSensedValue, previousSensedValue)){
				passedLine = true ;
				try{Thread.sleep(100);} catch (Exception e){};
			}
			else {
				passedLine = false ;
				try{Thread.sleep(25);} catch (Exception e){};
			}
			
			
			
		}
		
		//shuts off light to save the earth
		colorSensor.setFloodlight(false);
	}
	
	/**
	 * pause the lineReader XX ms,
	 * the underlying colorSensor will continue to work but 
	 * LineReader.getLightValue() will not be altered
	 * @param millisec
	 */
	public void pauseLineReader(int millisec){
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
		}
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
		//TIME TO AVOID false positive at the beginning of the robot movement 
		long waitTimeBeforeStart =50;
		int lightSensorThreshold = 5 ;// how sensitive sensor should be when it detects changes
		int ignorePeriod = 500 ; //time in ms to ignore further input 
		boolean hasDetected = ((previousSensedValue - currentSensedValue ) > lightSensorThreshold) ;
		boolean result;
		long diffInDectectionTime ; // time between positive feedbacks from sensor 
		//avoid detecting a line twice : two valid detection has to be 800 ms appart 
		diffInDectectionTime = (System.currentTimeMillis() - waitTimeBeforeStart - sensorStartTime);
		
		//if this is a new line and should be counted
		if (hasDetected && (diffInDectectionTime > ignorePeriod)){
			waitTimeBeforeStart = System.currentTimeMillis();
			result = hasDetected;
		}
		//if this is a false positive
		else if (hasDetected){
			waitTimeBeforeStart = System.currentTimeMillis();
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
