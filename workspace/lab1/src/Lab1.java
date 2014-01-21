import lejos.nxt.*;

public class Lab1 {
	
	private static final SensorPort usPort = SensorPort.S1;
	//private static final SensorPort lightPort = SensorPort.S2;
	//for Bang Bang
	private static final int BAND_CENTER = 29, BAND_WIDTH = 3;
	
	//for Pcontrol
	private static final int P_BAND_CENTER = 29, P_BAND_WIDTH = 3;
	
	private static final int MOTOR_LOW = 100, MOTOR_HIGH = 400;
	
	
	public static void main(String [] args) {
		/*
		 * Wait for startup button press
		 * Button.ID_LEFT = BangBang Type
		 * Button.ID_RIGHT = P Type
		 */
		
		int option = 0;
		Printer.printMainMenu();
		while (option == 0)
			option = Button.waitForAnyPress();
		
		// Setup controller objects
		BangBangController bangbang = new BangBangController(BAND_CENTER, BAND_WIDTH, MOTOR_LOW, MOTOR_HIGH);
		PController p = new PController(P_BAND_CENTER, P_BAND_WIDTH);
		
		// Setup ultrasonic sensor
		UltrasonicSensor usSensor = new UltrasonicSensor(usPort);
		
		// Setup Printer
		Printer printer = null;
		
		// Setup Ultrasonic Poller
		UltrasonicPoller usPoller = null;
		
		switch(option) {
		case Button.ID_LEFT:
			usPoller = new UltrasonicPoller(usSensor, bangbang);
			printer = new Printer(option, bangbang);
			break;
		case Button.ID_RIGHT:
			usPoller = new UltrasonicPoller(usSensor, p);
			printer = new Printer(option, p);
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
		
		usPoller.start();
		printer.start();
		
		//Wait for another button press to exit
		Button.waitForAnyPress();
		System.exit(0);
		
	}
}
