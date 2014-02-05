package lab4;

import lejos.nxt.NXTRegulatedMotor;

public class ZigZag extends Driver implements Drivable {

	ZigZag (RobotConfiguration config){
		super(config);
	}
	
	public void run(){
			travelTo(new Coordinate(60,30,0)); //up 30 cm 
			travelTo(new Coordinate(30,30,0));
			travelTo(new Coordinate(30,60,0));
			travelTo(new Coordinate(60,-2,0));
			
			//end of the run
			config.setDriveComplete(true);
	}


}
