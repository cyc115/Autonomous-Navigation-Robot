package other;

import coreLib.AbstractConfig;
import coreLib.ArmMotor;
import coreLib.Configuration;
import coreLib.Coordinate;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.Odometer;
import coreLib.UltrasonicPoller;
import ultrasonicListeners.Localize;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.comm.RConsole;

public class Lab5P2 {
	private static Driver driver = Driver.getInstance();
	private static AbstractConfig config = Configuration.getInstance();
	private static ColorSensor cs = new ColorSensor(AbstractConfig.LIGHT_SENSOR_PORT);
	
	public static void main(String[] args) {
		
		LCDWriter lcd = LCDWriter.getInstance();
		Odometer odo = Odometer.getInstance();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		Driver driver = Driver.getInstance();
		driver.start();
		lcd.start();
		odo.start();
		usp.start();
		
		lcd.writeToScreen("start" , 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
//		localize(); //now facing the diagonal  //works 

		ArmMotor.open();
		try {Thread.sleep(1000);} catch(Exception e){};
		
		driver.rotateToRelatively(90, true);
		int dist = usp.getDistance();
		while (true){
			RConsole.println(Math.toDegrees(odo.getTheta()) + "\t\t" + dist +"");
			if (usp.objectDetected()) 	{
				try {Thread.sleep(500);}catch(Exception e){};
				RConsole.println("item found. stop motor and move forward");
				break;	//break out and continue 
			}
			try {Thread.sleep(50);} catch(Exception e){};
		}
		
		//go to block 
		try {Thread.sleep(500);} catch(Exception e){};
		driver.motorStop();
		try {Thread.sleep(100);} catch(Exception e){};
		driver.motorForward();
		
		//check color 
		while (true){
			
			if (usp.getDistance() <= 11){
				driver.motorStop();
				
				if (isStyrofoam()){
					grab();
					cs.setFloodlight(false);
					pushToCorner();	
					break;
				}
				else { // !is styrofoam 
					goToNextWayPoint();
				}
			}
			else{RConsole.println("dist:" +(dist)+"");}
			try {Thread.sleep(50);} catch(Exception e){};
		}
		
		
	}
	private static void goToNextWayPoint() {
		driver.backward(10);
	}
	private static void grab() {
		ArmMotor.close();
	}
	private static void openArm() {
		ArmMotor.open();
	}
	/**
	 * returns true if the distance detected is a wall.
	 * @return
	 */
	private static boolean isWall() {
		return false;
	}

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
		boolean isStyrofoam = false ;
		boolean found = false ;
		
		cs.setFloodlight(true);
		try{Thread.sleep(500);}catch(Exception e){};
		Color c = cs.getColor();
		
		while (!found){
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			if (c.getRed() < 10 || b <10 || b < 10 ){
				LCDWriter.getInstance().writeToScreen("nothing found", 2);
			}

			else if ((double)c.getRed()/c.getBlue() >1.2){
				RConsole.println("RGB" + r + "\t" + g + "\t" + b);
				found = true ;
				isStyrofoam = false ;
				break ;
			}
			else {
				RConsole.println("RGB" + r + "\t" + g + "\t" + b);
				found = true;
				isStyrofoam = true ;
				break;
			}
			LCDWriter.getInstance().writeToScreen((((double)r/b >1.2) ? 
					"br" : "fo" ) + r + " " + g, 2);
		}

		return isStyrofoam ;
		
	}

	private static void pushToCorner() {
		driver.travelTo(30,90);
		
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

