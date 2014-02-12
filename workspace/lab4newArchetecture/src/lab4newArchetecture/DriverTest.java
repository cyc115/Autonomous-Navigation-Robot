package lab4newArchetecture;

public class DriverTest extends Driver implements Drivable{

	public DriverTest() {
		super(AbstractConfig.getInstance());
	}

	public void run(){
		
		travelTo(30,0);
		travelTo(30,30);
		travelTo(0,30);
		travelTo(0,0);
		AbstractConfig.getInstance().setDriveComplete();
	}

	
}
