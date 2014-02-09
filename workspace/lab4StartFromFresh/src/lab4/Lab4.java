package lab4;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Lab4 {
	RobotConfiguration config ;
	
	Lab4(){
		this.config = Configuration.getDefaultLab4();
	}
	
	public static void main (String [] args){
		Lab4 lab4 = new Lab4();  //the top most monitor class	
		
		int buttonChoice ;

		
		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Fallin| Raisin ", 0, 2);
			LCD.drawString("       |        ", 0, 3);
			LCD.drawString("       |        ", 0, 4);
			
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {
			lab4.initializeMotor();			//start 
			lab4.config.leftButtonPressed(); 

			Odometer odo = lab4.config.getOdometer();
			while(!lab4.config.driveComplete()){
				lab4.config.writeToMonitor("DIS" + lab4.config.getUsPoller().getDistance(), 3);
				lab4.config.writeToMonitor("O " + lab4.config.getCurrentLocation(), 7);
				lab4.config.writeToMonitor("LT " + String.valueOf(lab4.config.getLineReader().getLightValue()) , 1);
			}
			
		}
		else if (buttonChoice == Button.ID_RIGHT){
			lab4.initializeMotor();			//start 
			lab4.config.rightButtonPressed(); 

			Odometer odo = lab4.config.getOdometer();
			while(!lab4.config.driveComplete()){
				lab4.config.writeToMonitor("DIS" + lab4.config.getUsPoller().getDistance(), 3);
				lab4.config.writeToMonitor("O " + lab4.config.getCurrentLocation(), 7);
				lab4.config.writeToMonitor("LT " + String.valueOf(lab4.config.getLineReader().getLightValue()) , 1);
			}
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);

	}
	
	NXTRegulatedMotor [] getAllMotors(){
		return new NXTRegulatedMotor [] {RobotConfiguration.LEFT_MOTOR , RobotConfiguration.RIGHT_MOTOR};
	}
	
	void writeToMonitor(String s, int lineNumber){
		this.config.getMonitor().writeToScreen(s, lineNumber);
	}
	
	void initializeMotor(){
		this.config.resetMotorSpeed();
	}

	
}
 