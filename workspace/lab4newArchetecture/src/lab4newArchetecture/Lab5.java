package lab4newArchetecture;

import lejos.nxt.Button;

public class Lab5 {
	public static void main(String [] args){
		AbstractConfig config = Configuration.getInstance();
		LCDWriter.getInstance().start();;
		Odometer.getInstance().start();
		LineReader.getInstance().start();
		DriverTest dt = new DriverTest();
		dt.start();
		
		LCDWriter lcd = LCDWriter.getInstance();
		while(!config.isDriveComplete()){
			lcd.writeToScreen("x " + Odometer.getInstance().getX() + "", 0);
			lcd.writeToScreen("Y " + Odometer.getInstance().getY() + "", 1);
			lcd.writeToScreen("T " + Odometer.getInstance().getTheta(), 2);
			
		}
		//TODO driver need to be tested 
		
		//TODO add a getter to the threadStart
		//TODO how to stop a thread . right now threadStart will never return to false...
		
		while(Button.waitForAnyPress() != Button.ID_ESCAPE){}
		
		System.exit(0);
	}
}
