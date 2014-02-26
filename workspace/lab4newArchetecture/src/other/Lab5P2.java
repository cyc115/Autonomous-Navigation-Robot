package other;

import java.util.Stack;

import coreLib.AbstractConfig;
import coreLib.ArmMotor;
import coreLib.Configuration;
import coreLib.Coordinate;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.Odometer;
import coreLib.Point;
import coreLib.UltrasonicPoller;
import ultrasonicListeners.BlockInFrontInterrupt;
import ultrasonicListeners.Localize;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;

public class Lab5P2 {
	private static Driver driver = Driver.getInstance();
	private static AbstractConfig config = Configuration.getInstance();
	private static ColorSensor cs = new ColorSensor(AbstractConfig.LIGHT_SENSOR_PORT);
	private static Stack <Coordinate> wayPoints = new Stack<Coordinate>();
	


	private static LCDWriter lcd = LCDWriter.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	
	private static boolean navigationInterrupted = false ;
	
	public static void main(String[] args) {
		

		BlockInFrontInterrupt bifi = new BlockInFrontInterrupt();
		bifi.setCalled(false).setContinuous(true).setDistanceOnInvoke(15);
		driver.start();
		lcd.start();
		odo.start();
		usp.start();
		

		
		lcd.writeToScreen("start" , 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
//		localize(); //now facing the diagonal  //works 

		//handles the movement when there's a block in front
		usp.subscribe(bifi);
		if (usp.containsListener(bifi)){
			Sound.beep();
			RConsole.println("dOnInv" + bifi.getDistanceOnInvoke() );
		}
		wayPoints.push(new Coordinate(60, 180, 0));
		wayPoints.push(new Coordinate(0, 165, 0));
		wayPoints.push(new Coordinate(60, 165, 0));
		wayPoints.push(new Coordinate(60, 135, 0));
		wayPoints.push(new Coordinate(0, 135, 0));
		wayPoints.push(new Coordinate(0, 105, 0));
		wayPoints.push(new Coordinate(60,105,0));
		wayPoints.push(new Coordinate(60,60,0));
		
		while (!wayPoints.empty()){
			if (!navigationInterrupted){
				driver.rotateToRelatively(45);
				driver.rotateToRelatively(-90);
				
				driver.travelTo(wayPoints.peek());	
			}
			else {try {Thread.sleep(500);} catch (Exception e){}; }
			
			//if current location is near waypoint then consider it on dot and remove it 
			if (wayPoints.peek().isNear(new Coordinate (odo.getX(),odo.getY(),0))){
				wayPoints.pop();
			}
		}
		

		config.setDriveComplete();
		System.exit(0);

		////////////////////
		atWayPoint();
		
		ArmMotor.open();
		try {Thread.sleep(1000);} catch(Exception e){};
		
		//scan for item 
		scanForItem(usp,odo);
		
		
		
		action1(usp);

		System.exit(0);
	}
	private static void atWayPoint() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * moves the robot to the item, checks if the item is 
	 * a block 
	 * 			-(true)->
	 *  			backUp and goto NextWayPoint.
	 * <br> 	-(false)-> a foam 
	 * 				grab and move to destination 				
	 * 
	 * @param usp
	 */
	public static void action1(UltrasonicPoller usp) {
		navigationInterrupted = true ;
		
		if (goToItemAndCheck(usp)){
			cs.setFloodlight(false);
			grab();
			pushToCorner();	
		}
		else {
			cs.setFloodlight(false);
			backUp();
			goToNextWayPoint();
		}
		
		navigationInterrupted = false ;
	}
	
	/**
	 * clears the wayPoint stack 
	 */
	public static void clearStack(){
		while (!wayPoints.empty()){
			wayPoints.pop();
		}
	}
	
	/**
	 * goes to the item infront and check 
	 * if it is a foam or brick
	 * @return true if is froam
	 */
	private static boolean goToItemAndCheck(UltrasonicPoller usp) {
		boolean isFoam = false ;
		
		//go to item 
		driver.motorStop();
		Sound.beep();
		try {Thread.sleep(100);} catch(Exception e){};
		
		isFoam = isStyrofoam();
		return isFoam;
		
	}
	private static void scanForItem(UltrasonicPoller usp,Odometer odo ) {
		driver.rotateToRelatively(90, true);
		int dist = usp.getDistance();
		while (true){
			RConsole.println(Math.toDegrees(odo.getTheta()) + "\t\t" + dist +"");
			if (usp.objectDetected()) 	{
				try {Thread.sleep(300);}catch(Exception e){};
				RConsole.println("item found. stop motor and move forward");
				break;	//break out and continue 
			}
			try {Thread.sleep(50);} catch(Exception e){};
		}
	}
	private static void backUp() {
		driver.backward(12);
	}
	private static void goToNextWayPoint() {
		int shift = 0 ;
		if (odo.getX() > 45 ) {
			shift = -25;
			Sound.beep();
			Sound.beep();
			
		}
		else {
			shift = 25;
			Sound.beep();
		}
		wayPoints.push(new Coordinate(odo.getX() + shift, odo.getY() + Math.abs(shift) , 0 ) );
		wayPoints.push(new Coordinate(odo.getX() + shift, odo.getY(), 0 ) );
		
		UltrasonicPoller.enableULinsteners();
		navigationInterrupted = false ;
	}
	private static void grab() {
		
		driver.forward(5);
		//close light 
		cs.setFloodlight(false);
		ArmMotor.close();
	}
	private static void openArm() {
		ArmMotor.open();
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
			clearStack();
			wayPoints.push(new Coordinate (60,180,0));
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
	
	public static Stack<Coordinate> getWayPoints() {
		return wayPoints;
	}
}

