package lab4newArchetecture;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class Lab5P2 {

	public static void main(String[] args) {
		
		LCDWriter.getInstance().writeToScreen("start" , 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
		localize(); //now facing the diagonal 
//		findStyrofoam();
//		pushToCorner();
	}

	private static void localize(){
		AbstractConfig config = Configuration.getInstance();
		Driver.setSpeed(config.getRotationSpeed());
		UltrasonicPoller upoller = UltrasonicPoller.getInstance();
		config.stopMotor();
		
		upoller.start();
		Driver driver = Driver.getInstance();
		
		Localize loc = new Localize(30, true);
		upoller.subscribe(loc);
		driver.rotateToRelatively(360);
		
	}
}

