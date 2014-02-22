package other;

import java.util.Stack;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;
import coreLib.AbstractConfig;
import coreLib.ArmMotor;
import coreLib.Configuration;
import coreLib.Coordinate;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.Odometer;
import coreLib.UltrasonicPoller;

public class Tester {
	private static AbstractConfig config = Configuration.getInstance();
	
	public static void main(String[] args) {
		LCDWriter lcd = LCDWriter.getInstance();
		Odometer odo = Odometer.getInstance();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		Driver driver = Driver.getInstance();
		RConsole.openAny(5000);
				
		lcd.start();
		odo.start();
		usp.start();
		driver.start();
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		lcd.writeToScreen("started", 0);
		
		config.setCurrentLocation(new Coordinate(30, 30, 0));
		
		Stack<Coordinate> st = new Stack<Coordinate>();
		st.push(new Coordinate(30, 90, 0));
		st.push(new Coordinate(90, 90, 0));
		st.push(new Coordinate(90, 30, 0));
		st.push(new Coordinate(30, 30, 0));
		
		
		
	}


}
