package lab4newArchetecture;

import lejos.nxt.Button;

public class Lab5 {
	public static void main(String [] args){
		AbstractConfig config = Configuration.getInstance();
		LCDWriter.getInstance().start();;
		Odometer.getInstance().start();
		LCDWriter.getInstance().writeToScreen("done", 0);
		LineReader.getInstance().start();
		
		//TODO add a getter to the threadStart
		//TODO how to stop a thread . right now threadStart will never return to false...
		
		while(Button.waitForAnyPress() != Button.ID_ESCAPE){}
		
		System.exit(0);
	}
}
