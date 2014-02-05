package lab3StartFromFresh;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Lab3 {
	RobotConfiguration config ;
	
	Lab3(RobotConfiguration config){
		this.config = config;
	}
	Lab3(){
		this.config = Configuration.getDefaultLab3Config();
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

			lab3.initializeMotor();			//start 
			lab3.config.leftButtonPressed(); 

//			TODO remove for the 4th lab 
			Odometer odo = lab3.config.getOdometer();
			UsPoller poller = new UsPoller(lab3.config);
			while(!lab3.config.driveComplete()){
				lab3.config.writeToMonitor(String.valueOf(lab3.config.getPlanner().isWallFollow()),4);
//				lab3.config.writeToMonitor(lab3.config.getCurrentLocation().toString(), 4);
				lab3.config.writeToMonitor("X:"+odo.getX(), 5);
				lab3.config.writeToMonitor("Y:"+odo.getY(), 6);
				lab3.config.writeToMonitor("T:"+odo.getThetaInDeg(), 7);

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
 