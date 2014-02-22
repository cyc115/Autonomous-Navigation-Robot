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
		
		LCDWriter lcd = LCDWriter.getInstance();
		Odometer odo = Odometer.getInstance();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		lcd.start();
		odo.start();
		usp.start();
		
		lcd.writeToScreen("start" , 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
//		localize(); //now facing the diagonal  //works 
		
		Driver driver = Driver.getInstance();
		driver.start();
		driver.turnTo(-45);
		driver.rotateToRelatively(90, true);
		
		while(!itemDetected()){
			try {Thread.sleep(25);} catch(Exception e){};
		}
		
		//go to block 
		driver.motorStop();
		try {Thread.sleep(100);} catch(Exception e){};
		driver.motorForward();
		
		while(!itemInfront()){	try {Thread.sleep(100);} catch(Exception e){}; } 			//don't stop 
		
		//if item is styrofoam 
		if(recognizeItem()){
			//grab and go to destination
		} 
		else {
			// do other things 
		}
		
		
		
		
//		Driver.setSpeed(70);

//		while (!styrofoamFound){
//			lookForBlock();
//			styrofoamFound = isStyrofoam();
//		}
		pushToCorner();	}
	
	/**
	 * recognize if the item is a block or styrofoam 
	 * @return true if styrofoam 
	 */
	private static boolean recognizeItem() {
		
		return false;
	}
	/**
	 * return true if there is an item in front, 
	 * an item is a block, not a wall 
	 * @return
	 */
	private static boolean itemInfront() {
		return false;
	}
	/**
	 * if the robot is close enough to the block and can turn on color senor
	 * @return
	 */
	private static boolean itemDetected() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * return true if this is a styrofoam 
	 * @return
	 */
	private static boolean isStyrofoam() {
		// TODO Auto-generated method stub
		return false ;
		
	}

	private static void pushToCorner() {
		// TODO Auto-generated method stub
		
	}

	private static void lookForBlock() {
		boolean blockFound = false ;
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		
		Driver.setSpeed(40);
		int delta ; 
		
		while(!blockFound){
			double angle1 = 0 , angle2 = 0  , center ;
			int prevDist, currentDist = usp.getDistance() ;
			int THREASHOLD = 4;
			driver.rotateToRelatively(-90,false);

			driver.rotateToRelatively(90);
			prevDist = currentDist;
			currentDist = usp.getDistance();

			delta = prevDist - currentDist;
			
			if (delta> THREASHOLD){
				angle1 = Odometer.getInstance().getTheta();
				RConsole.println("angl1");
			}
			else if (delta  < THREASHOLD){
				angle2 = Odometer.getInstance().getTheta();
				Driver.getInstance().motorStop(); 		
				blockFound = true ;
				RConsole.println("angl2");
			}
			
				
		}
		
		driver.motorForward();
		int prevDist, currentDist;
		int THREASHOLD = 10;
		double angle1 = 0 , angle2 = 0  , center ;
		currentDist = usp.getDistance();

		while (!blockFound){
//			prevDist = currentDist;
//			currentDist = usp.getDistance();
//			Driver.getInstance().rotateToRelatively(180,true);
//			int delta = prevDist - currentDist;
//			if (delta> THREASHOLD){
//				angle1 = Odometer.getInstance().getTheta();
//			}
//			else if (delta  < THREASHOLD){
//				angle2 = Odometer.getInstance().getTheta();
//				Driver.getInstance().motorStop();
//				blockFound = true ;
//			}
//			try {Thread.sleep(100);}catch(Exception e){};
//			RConsole.println("delta" + delta);
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

