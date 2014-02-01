package lab3StartFromFresh;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;

public class Lab3 {
	RobotConfiguration config ;
	
	Lab3(RobotConfiguration config){
		this.config = config;
	}
	Lab3(){
		this.config = Configuration.getNewDefaultConfiguration();
	}
	
	public static void main (String [] args){
		Lab3 lab3 = new Lab3();  //the top most monitor class
		
		int buttonChoice ;
		
		
		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" part 1| Drive  ", 0, 2);
			LCD.drawString("       | in a   ", 0, 3);
			LCD.drawString("       | square ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		
		if (buttonChoice == Button.ID_LEFT) {
			for(NXTRegulatedMotor motor : lab3.getAllMotors()){
				motor.forward();
				motor.flt();
			}
			lab3.startLCDMonitor();
			lab3.writeToMonitor("hello world", 0);
			
			
		}
		else {
			
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
		
		
		
	}
	
	NXTRegulatedMotor [] getAllMotors(){
		return config.getAllMotors();
	}
	
	/**
	 * start to monitor the device
	 */
	void startLCDMonitor(){
		this.config.getMonitor().start();
	}
	void writeToMonitor(String s, int lineNumber){
		this.config.getMonitor().writeToScreen(s, lineNumber);
	}
	

	
}
