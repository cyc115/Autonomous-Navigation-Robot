package coreLib;

import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;

public class BlockRecognizer extends Thread {
	
	private static ColorSensor colorSensor;
	private AbstractConfig config;
	private boolean threadStarted  = false ;

	private static BlockRecognizer instance ;
	
	private BlockRecognizer(AbstractConfig config){
		this.config = config; 
		colorSensor = new ColorSensor(AbstractConfig.LIGHT_SENSOR_PORT);
	}
	
	public static BlockRecognizer getInstance(){
		if (colorSensor == null){
			instance = new BlockRecognizer(AbstractConfig.getInstance());
		}
		return instance;
	}
	
	
	public void run(){
		colorSensor.getRawColor();
		
	}
	
	public boolean isThreadStarted() {
		return threadStarted;
	}

	public void setThreadStarted(boolean threadStarted) {
		this.threadStarted = threadStarted;
	}


}
