package lab4;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UsPoller extends Thread {
	private int SLEEP_INTERVAL = 20 ;
	RobotConfiguration config;
	private int distance = 255; //initialize the distance read to -1
	UltrasonicSensor uSensor;
	
	UsPoller (RobotConfiguration config){
		this.config = config;
		uSensor = new UltrasonicSensor(RobotConfiguration.ULTRASONIC_SENSOR_PORT);
		uSensor.continuous();
	}
	
	
	public void run (){
		while (true){
			distance = uSensor.getDistance();
			try { Thread.sleep(SLEEP_INTERVAL); } catch(Exception e){};
		}
	}
	/**
	 * distance refresh rate 20hz 
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
}
