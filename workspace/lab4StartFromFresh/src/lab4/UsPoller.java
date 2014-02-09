package lab4;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UsPoller extends Thread {
	private int SLEEP_INTERVAL = 20 ;
	RobotConfiguration config;
	private int distance = 15; //initialize the distance read to 25
	UltrasonicSensor uSensor;
	
	UsPoller (RobotConfiguration config){
		this.config = config;
		uSensor = new UltrasonicSensor(RobotConfiguration.ULTRASONIC_SENSOR_PORT);
		uSensor.continuous();
		
		//filter the distance and get 10 times polling avg
		double mean = 0 ;
		for (int i = 0 ; i < 10 ; i ++ ){
			mean = ((mean*i) + uSensor.getDistance())/i;
		}
		distance = (int) mean ;
		
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
