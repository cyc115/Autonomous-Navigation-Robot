package lab4;

import lejos.nxt.comm.RConsole;

public class mapping {
	
	Configuration config;
	
	public mapping() {
		this.config = Configuration.getDefaultMapping();
		
	}
	
	public static void main(String[] args) {
		mapping map = new mapping();
		UsPoller up = new UsPoller(map.config);
		up.start();
		try{Thread.sleep(200);}catch(Exception e){};
		
		for (int i = 0 ; i <= 360 ; i+=3){
			map.config.SENSOR_MOTOR.rotate(3);
			RConsole.println(i + "\t" + (up.getDistance() > 45 ? 45 : up.getDistance()));			
		}
		RConsole.println("=====================================");			
		for (int i = 0 ; i <= 360 ; i+=3){
			map.config.SENSOR_MOTOR.rotate(-3);
			RConsole.println(i + "\t" + (up.getDistance() > 45 ? 45 : up.getDistance()));
		}
		
		

	}

}
