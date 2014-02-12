package lab4newArchetecture;

import lejos.nxt.Button;

public class Lab5Part1 {

	public static void main(String [] args){
		AbstractConfig config = Configuration.getInstance();
		LCDWriter.getInstance().start();
		UltrasonicPoller uspoller = UltrasonicPoller.getInstance();
		uspoller.start();
//		Odometer.getInstance().start();
//		LineReader.getInstance().start();
//		DriverTest dt = new DriverTest();
		
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
		LCDWriter lcd = LCDWriter.getInstance();		
		Driver.motorForward();
		
		while (uspoller.getDistance() > 10 ){try{Thread.sleep(30);}catch (Exception e) {}}
		Driver.motorStop();
		
		
		//TODO add a getter to the threadStart
		//TODO how to stop a thread . right now threadStart will never return to false...
		
		while(Button.waitForAnyPress() != Button.ID_ESCAPE){}
		
		System.exit(0);
	}

	
	
}
