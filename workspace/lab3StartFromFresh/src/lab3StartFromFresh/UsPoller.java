package lab3StartFromFresh;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UsPoller extends Thread {
	private int distance = -1;
	UltrasonicSensor uSensor = new UltrasonicSensor(SensorPort.S1);
	RobotConfiguration config;
	
	UsPoller (RobotConfiguration config){
		this.config = config;
		uSensor.continuous();
	}
	
	
	public void run (){
		while (true){
			distance = uSensor.getDistance();

			try { Thread.sleep(50); } catch(Exception e){};
			config.writeToMonitor(String.valueOf(distance), 0);
		}
	}
	public int getDistance() {
		return distance;
	}
}
