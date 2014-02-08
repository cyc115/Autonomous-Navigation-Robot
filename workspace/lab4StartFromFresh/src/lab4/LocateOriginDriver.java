package lab4;

public abstract class LocateOriginDriver extends Driver {
	int deg = 360 ;
	int turningDeg = 2;
	int distance = 30 ; //angle to use when distance from sensor falls below this 
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
//		RobotConfiguration.LEFT_MOTOR.forward();
//		RobotConfiguration.RIGHT_MOTOR.forward();
//		while( !config.getLineReader().isPassedLine() ){ /*wait*/	} 
//		
//		config.stopMotor();
		
		//go back 11 cm for the wheel to be about the center
//		config.getDriver().travel(-11);
		
		//rotate left to see line and set that line to be the origin
		rotateToRelatively(-180, true);
		
		while(!config.getLineReader().isPassedLine()){ /*do nothing */	}
		config.stopMotor();
		
		
		config.setDriveComplete(true);
	}


}
