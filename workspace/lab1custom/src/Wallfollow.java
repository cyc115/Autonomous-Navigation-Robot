import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class Wallfollow {
	private static final SensorPort usPort = SensorPort.S1;
	//private static final SensorPort lightPort = SensorPort.S2;
	
	private static final int bandCenter = 20, bandWidth = 3;
	private static final int motorLow = 100, motorHigh = 400;
	private static final int NOT_PRESSED = 0 ; 
	//sensors and printer
	UltrasonicSensor usSensor = new UltrasonicSensor(usPort);
	static Printer printer = null;
	
	public static void main(String [] args){
		/*
		 * Wait for startup button press
		 * Button.ID_LEFT = BangBang Type
		 * Button.ID_RIGHT = P Type
		 */
		int buttonOption = NOT_PRESSED;
		Printer.printMainMenu();
		
		while (buttonOption == NOT_PRESSED)
			buttonOption = Button.waitForAnyPress();
		
		//if button is pressed : 
		switch(buttonOption) {
		case Button.ID_LEFT:
			//Bang bang 
			BangBang bangControl = new BangBang(usPort);
			//start the whole sequence
			bangControl.start();
			break ;
		case Button.ID_RIGHT:
			// P control 
			PControl pc = new PControl();
			pc.start();
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
		printer.start();
		
		//Wait for another button press to exit
		Button.waitForAnyPress();
		System.exit(0);
		
		
		
	}
	
}
