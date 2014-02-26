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
		bifi.setCalled(false).setContinuous(true);
		driver.start();
		lcd.start();
		odo.start();
		usp.start();
		

		
		lcd.writeToScreen("start" , 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
		localize(); //now facing the diagonal  //works 

		//handles the movement when there's a block in front
		usp.subscribe(bifi);
		if (usp.containsListener(bifi)){
			Sound.beep();
			RConsole.println("dOnInv" + bifi.getDistanceOnInvoke() );
		}
		wayPoints.push(new Coordinate(80, 180, 0));
		wayPoints.push(new Coordinate(0, 165, 0));
		wayPoints.push(new Coordinate(30, 165, 0));
		wayPoints.push(new Coordinate(60, 165, 0));
		wayPoints.push(new Coordinate(60, 135, 0));
		wayPoints.push(new Coordinate(30, 135, 0));
		wayPoints.push(new Coordinate(0, 135, 0));
		wayPoints.push(new Coordinate(0, 105, 0));
		wayPoints.push(new Coordinate(30,105,0));
		wayPoints.push(new Coordinate(30,60,0));
		
		while (!wayPoints.empty()){
			if (!navigationInterrupted){
				scanAtWayPoint();
				int len = wayPoints.size();
				//if current y is bigger than the waypoint's y then pop
				for (int i = 0  ; i < len ; i++ ){ 
					try {
						if (odo.getY() > wayPoints.elementAt(i).getY() ){
							wayPoints.pop();
						}
					} catch(Exception e){
						//TODO Stack.size() dies not return the correct size..... so catch the exception and move on  
					} 
				} 
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

	}
	/**
	 * Scan for block or foam at the waypoint 
	 */
	private static void scanAtWayPoint() {
		if (!navigationInterrupted)	driver.rotateToRelatively(55);
		if (!navigationInterrupted) driver.rotateToRelatively(-110);
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
		driver.motorStop();
		
//		driver.forward(3);
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
	 * goes to the item and stop infront of it. beep and stop motor 
	 * then execute isStyrofoam()
	 * if it is a foam or brick
	 * @return true if is froam
	 */
	private static boolean goToItemAndCheck(UltrasonicPoller usp) {
		boolean isFoam = false ;
		//go to item 
		driver.motorStop();
		
		/*
		driver.rotateToRelatively(-60);
		driver.rotateToRelatively(180,true);
		int cDist = usp.getDistance(), pDist = cDist; 
		RConsole.println("Rotating======");
		int count = 0 ; //make sure the loop always breaks 
		while( cDist >= pDist && !driver.isMotorStopped() && count < 200){
			pDist = cDist ;
			cDist = usp.getDistance();
			RConsole.println("CD/PD: " + cDist + "  " + pDist );
			try{Thread.sleep(10);} catch (Exception e){};
			count ++ ;
		}
		
		*/
		
		
		Sound.beep();
		driver.motorStop();
		RConsole.println("motor is stoped");
		try{Thread.sleep(100);} catch (Exception e){};
		//move 5 cm to get closer to the block 
		RConsole.println("Drive forward 5");
		driver.forward(5);
		try{Thread.sleep(1000);} catch (Exception e){};
		RConsole.println("check for foam");
		driver.motorStop();
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
		driver.backward(17);
	}
	private static void goToNextWayPoint() {
		int shift = 0 ;
		if (odo.getX() > 45 ) {
			shift = -35;
			Sound.beep();
			Sound.beep();
			
		}
		else {
			shift = 35;
			Sound.beep();
		}
		wayPoints.push(new Coordinate(odo.getX() + shift, odo.getY() + Math.abs(shift) , 0 ) );
		wayPoints.push(new Coordinate(odo.getX() + shift, odo.getY(), 0 ) );
		
		UltrasonicPoller.enableULinsteners();
		navigationInterrupted = false ;
	}
	private static void grab() {
		driver.motorStop();
		try {Thread.sleep(200);} catch(Exception e){};
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
		
		driver.motorStop();
		
		cs.setFloodlight(true);
		try{Thread.sleep(500);}catch(Exception e){};
		Color c = cs.getColor();

		long time = System.currentTimeMillis();
		while (!found){
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			
			if(r >=5 && b >= 5){
				LCDWriter.getInstance().writeToScreen(r + " " + g + " " + b ,2 );
				if ( r/(double)b > 1.2){
					found = true ;
					isStyrofoam = false ;
					break ;
				} else {
					found = true;
					isStyrofoam = true ;
					break;
				}
			} 
			else { //if can't find something for 4s break 
				LCDWriter.getInstance().writeToScreen("nothing found", 2);
				if (System.currentTimeMillis() - time > 4000 ) {
					isStyrofoam = false;
					Sound.beepSequenceUp();
					break ;
				}
			}
		}

		return isStyrofoam ;
		
	}

	private static void pushToCorner() {
			clearStack();
			wayPoints.push(new Coordinate (80,180,0));
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

