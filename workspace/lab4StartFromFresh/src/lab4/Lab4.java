package lab4;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Lab4 {
	RobotConfiguration config ;
	
	Lab4(RobotConfiguration config){
		this.config = config;
	}
	Lab4(){
		this.config = Configuration.getDefaultLab3Config();
	}
	
	public static void main (String [] args){
		Lab4 lab3 = new Lab4();  //the top most monitor class	
		
		int buttonChoice ;
		
		
		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" rising| Drive  ", 0, 2);
			LCD.drawString("       | in a   ", 0, 3);
			LCD.drawString("       | square ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {

			lab3.initializeMotor();			//start 
			lab3.config.leftButtonPressed(); 

			Odometer odo = lab3.config.getOdometer();
			while(!lab3.config.driveComplete()){
				lab3.config.writeToMonitor("DIS" + lab3.config.getUsPoller().getDistance(), 3);
				lab3.config.writeToMonitor("WALL" + String.valueOf(lab3.config.getPlanner().hasWallAhead()),4);
				lab3.config.writeToMonitor("O " + lab3.config.getCurrentLocation(), 7);

			}
			
		}
		else if (buttonChoice == Button.ID_RIGHT){
			//TODO do something
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
 