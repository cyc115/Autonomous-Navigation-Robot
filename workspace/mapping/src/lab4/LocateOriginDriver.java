package lab4;

import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;

public abstract class LocateOriginDriver extends Driver {
	int deg = 360 ;
	int turningDeg = 2;
	int distance = 35 ; //angle to use when distance from sensor falls below this 
	double t1 = -1 , t2 = -1; //angle 1 and angle 2
	LineReader linereader ;
	
	public LocateOriginDriver(RobotConfiguration config) {
		super(config);
		
		
	}
	
	public abstract void run();

	/**
	 * this method handles common ending between rising and falling edge.
	 * when the robot is pointing 45 degree outward
	 */
	protected void driveAndLocate(){
		/**
		 * TODO line reader need to be initialized here or else it has 
		 * a nullpointer exception. I think there is some problem withe 
		 * the order of which i initialize things in config.
		 */
		config.setLineReader(new LineReader(config));
		linereader = config.getLineReader();
		linereader.start();

		//MOVE FORWARD UNTIL SEE A LINE 
		RobotConfiguration.LEFT_MOTOR.forward();
		RobotConfiguration.RIGHT_MOTOR.forward();
		while( !config.getLineReader().isPassedLine() ){
			/*wait*/	
			try{Thread.sleep(15);}catch(Exception e){}
		} 
		config.stopMotor();
		
		//go back 11 cm for the wheel to be about the center
		config.getDriver().travel(-10.5);
		
		//rotate left to see line and set that line to be the origin
		rotateToRelatively(-180, true);
		
		while(!config.getLineReader().isPassedLine()){ /*do nothing */	
			try{Thread.sleep(15);}catch(Exception e){}
		}
		config.stopMotor();
		
		/**
		 * angle for each of the lines in rad
		 */
		double t1 , t2 , t3, t4; 
		
		t1 = config.getCurrentLocation().getTheta();
		Sound.beep();
		RConsole.println("t1\t"+Math.toDegrees(t1));
		rotateToRelatively(-360, true);
		try{Thread.sleep(700);}catch(Exception e){}
		
		while (!config.getLineReader().isPassedLine()){try{Thread.sleep(15);}catch(Exception e){}};
		t2 = config.getCurrentLocation().getTheta();
		Sound.beep();
		RConsole.println("t2\t"+Math.toDegrees(t2));
		try{Thread.sleep(200);}catch(Exception e){}

		while (!config.getLineReader().isPassedLine()){try{Thread.sleep(15);}catch(Exception e){}};
		t3 = config.getCurrentLocation().getTheta();
		Sound.beep();
		RConsole.println("t3\t"+Math.toDegrees(t3));
		try{Thread.sleep(200);}catch(Exception e){}
		
		while (!config.getLineReader().isPassedLine()){try{Thread.sleep(15);}catch(Exception e){}};
		t4 = config.getCurrentLocation().getTheta();
		Sound.beep();
		RConsole.println("t4\t"+Math.toDegrees(t4));
		//calculate x 
		double x , y ;
		x = -10.5 *Math.cos((t3-t1)/2);
		y = -10.5 *Math.cos((t4-t2)/2);
		RConsole.println("X\t" + x);
		RConsole.println("y\t" + y);
		
		Coordinate c = config.getCurrentLocation();
		RConsole.println("current x: " + c.getX() 
				+ " Y: " + c.getY() +
				"Theata " + Math.toDegrees(c.getTheta()));
		
		config.getCurrentLocation().setTheta(0).setX(0).setY(0);
		RConsole.println("current Coordi" + config.getCurrentLocation().toString());
		try{Thread.sleep(1000);}catch(Exception e){};
		config.getDriver().travel(-x);
		config.getDriver().rotateToRelatively(100, true);
		while(!config.getLineReader().isPassedLine()){try{Thread.sleep(15);}catch(Exception e){}};
		
		try{Thread.sleep(1000);}catch(Exception e){};
		config.getDriver().travel(-y);
		config.getDriver().rotateToRelatively(-100, true);
		while(!config.getLineReader().isPassedLine()){try{Thread.sleep(15);}catch(Exception e){}};
		config.stopMotor();
		config.getDriver().rotateToRelatively(5, false);
		
		config.setDriveComplete(true);
	}


}
