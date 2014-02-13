package coreLib;


public class DriverTest extends Driver implements Drivable{

	public DriverTest() {
		super(AbstractConfig.getInstance());
	}

	public void run(){
		
		travelTo(new Coordinate(60,30,0)); //up 30 cm 
		travelTo(new Coordinate(30,30,0));
		travelTo(new Coordinate(30,60,0));
		travelTo(new Coordinate(60,-2,0));
		AbstractConfig.getInstance().setDriveComplete();
	}

	
}
