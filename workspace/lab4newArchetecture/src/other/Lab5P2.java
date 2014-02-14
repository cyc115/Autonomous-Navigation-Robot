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
	private static Driver driver = Driver.getInstance();
	private static boolean styrofoamFound = false ;
	
	public static void main(String[] args) {
		
		LCDWriter.getInstance().start();
		Odometer.getInstance().start();
		LCDWriter.getInstance().writeToScreen("start" , 1);
		UltrasonicPoller.getInstance().start();
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
//		localize(); //now facing the diagonal 
		Driver.setSpeed(70);
		Driver.getInstance().start();
		Driver.getInstance().rotateToRelatively(360,true);
		while (!styrofoamFound){
			lookForBlock();
			styrofoamFound = isStyrofoam();
		}
		pushToCorner();	}

	private static boolean isStyrofoam() {
		// TODO Auto-generated method stub
		return false ;
		
	}

	private static void pushToCorner() {
		// TODO Auto-generated method stub
		
	}

	private static void lookForBlock() {
		driver.motorForward();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		boolean blockFound = false ;
		int prevDist, currentDist;
		int THREASHOLD = 10;
		double angle1 = 0 , angle2 = 0  , center ;
		currentDist = usp.getDistance();
		Driver.setSpeed(40);
		while (!blockFound){
			prevDist = currentDist;
			currentDist = usp.getDistance();
			Driver.getInstance().rotateToRelatively(180,true);
			int delta = prevDist - currentDist;
			if (delta> THREASHOLD){
				angle1 = Odometer.getInstance().getTheta();
			}
			else if (delta  < THREASHOLD){
				angle2 = Odometer.getInstance().getTheta();
				Driver.getInstance().motorStop();
				blockFound = true ;
			}
			try {Thread.sleep(100);}catch(Exception e){};
			RConsole.println("delta" + delta);
		}
		center = ((angle1 + angle2)/2)- angle2 ;
		Driver.getInstance().rotateToRelatively(Math.toRadians(center));
		
	}

	private static void localize(){
		AbstractConfig config = Configuration.getInstance();
		Driver.setSpeed(config.getRotationSpeed());
		UltrasonicPoller upoller = UltrasonicPoller.getInstance();
		upoller.start();		
		
		Localize loc = new Localize(30, true);
		upoller.subscribe(loc);
		
		//start rotating 
		driver.rotateToRelatively(360);
		//localization process is called when the uspoller have certain value
				
		while (!loc.isDone()) {/* wait for localization to be finished */ try{Thread.sleep(1000);}catch(Exception e){}}		
		//clean up and unsbuscribe from uspoller 
		LCDWriter.getInstance().writeToScreen(upoller.unsubscribe(loc)? "unsubscribed" : "error ", 1);;
		
	}
}

