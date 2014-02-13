package other;

import coreLib.AbstractConfig;
import coreLib.Configuration;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.Odometer;
import coreLib.UltrasonicPoller;
import ultrasonicListeners.Localize;
import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class Lab5P2 {

	public static void main(String[] args) {
		
		LCDWriter.getInstance().start();
		Odometer.getInstance().start();
		LCDWriter.getInstance().writeToScreen("start" , 1);
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
		localize(); //now facing the diagonal 
		findStyrofoam();
//		pushToCorner();
	}

	private static void findStyrofoam() {
		
		Driver.setSpeed(AbstractConfig.getInstance().getForwardSpeed());
		Driver.motorForward();
		
	}

	private static void localize(){
		AbstractConfig config = Configuration.getInstance();
		Driver.setSpeed(config.getRotationSpeed());
		UltrasonicPoller upoller = UltrasonicPoller.getInstance();
		upoller.start();		
		
		Localize loc = new Localize(30, true);
		upoller.subscribe(loc);
		
		//start rotating 
		Driver.rotateToRelatively(360);
		//localization process is called when the uspoller have certain value
				
		while (!loc.isDone()) {/* wait for localization to be finished */ try{Thread.sleep(1000);}catch(Exception e){}}		
		//clean up and unsbuscribe from uspoller 
		LCDWriter.getInstance().writeToScreen(upoller.unsubscribe(loc)? "unsubscribed" : "error ", 1);;
		
	}
}

