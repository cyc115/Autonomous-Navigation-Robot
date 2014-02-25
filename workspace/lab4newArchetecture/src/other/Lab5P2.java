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
	
	public static void main(String[] args) {
		
		LCDWriter lcd = LCDWriter.getInstance();
		Odometer odo = Odometer.getInstance();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		Driver driver = Driver.getInstance();
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
		wayPoints.push(new Coordinate(0, 150, 0));
		wayPoints.push(new Coordinate(60,90,0));
		
		while (!wayPoints.empty()){
			driver.travelTo(wayPoints.peek());
			/**
			 * TODO during travelTo if we see a block, 
			 * the avoidance code will execute. but the travelTo()
			 * code is not stopped ... there's a conflict 
			 * TODO change the forward() in Driver to use forward instead of rotateTo
			 */
			if (wayPoints.peek().isNear(new Coordinate (odo.getX(),odo.getY(),0))){
				wayPoints.pop();
			}
		}
		
		////////////////////
		
		System.exit(0);
		
		atWayPoint();
		
		ArmMotor.open();
		try {Thread.sleep(1000);} catch(Exception e){};
		
		//scan for item 
		scanForItem(usp,odo);
		
		
		
		action1(usp);

		
		
		
		System.exit(0);
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
		if (goToItemAndCheck(usp)){
			grab();
			pushToCorner();	
		}
		else {
			backUp();
			goToNextWayPoint();
		}
	}
	
	/**
	 * clears the wayPoint stack 
	 */
	public static void clearStack(){
		while (!wayPoints.empty()){
			wayPoints.pop();
		}
	}
	private static void atWayPoint() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * goes to the item infront and check 
	 * if it is a foam or brick
	 * @return true if is froam
	 */
	private static boolean goToItemAndCheck(UltrasonicPoller usp) {
		boolean isFoam = false ;
		
		//go to item 
		try {Thread.sleep(500);} catch(Exception e){};
		driver.motorStop();
		try {Thread.sleep(100);} catch(Exception e){};
		driver.motorForward();
		
		while (true){
			//move towards block
			if (usp.getDistance() <= 11){
				driver.motorStop();
				
				if (isStyrofoam()){
					isFoam = true ;
					break;
				}
				else { // !is styrofoam 
					isFoam = false ;
					break;
				}
			}
			else{RConsole.println("dist:" +(usp.getDistance())+"");}
			try {Thread.sleep(50);} catch(Exception e){};
		}
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
		driver.backward(15);
	}
	private static void goToNextWayPoint() {
		driver.backward(10);
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

