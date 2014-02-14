package other;

import coreLib.AbstractConfig;
import coreLib.Coordinate;
import coreLib.Drivable;
import coreLib.Driver;


public class DriverTest implements Drivable{
	public static void main(String [] args){
		
		run();
	}
	

	public static void run(){
		Driver d = Driver.getInstance();
		d.rotateToRelatively(360,true);
		d.travelTo(new Coordinate(60,30,0)); //up 30 cm 
		d.travelTo(new Coordinate(30,30,0));
		d.travelTo(new Coordinate(30,60,0));
		d.travelTo(new Coordinate(60,-2,0));
		AbstractConfig.getInstance().setDriveComplete();
	}

	
}
