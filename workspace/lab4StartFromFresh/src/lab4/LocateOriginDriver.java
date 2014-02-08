package lab4;

public class LocateOriginDriver extends Driver implements Drivable {
	int deg = 360 ;
	int turningDeg = 5;
	int distance = 30 ; //angle to use when distance from sensor falls below this 
	double t1 = -1 , t2 = -1; //angle 1 and angle 2
	LineReader linereader ;
	
	public LocateOriginDriver(RobotConfiguration config) {
		super(config);
		
		
	}
	
	public void run(){
		//falling , see nothing -> see something 
		
		t1 = Math.toDegrees(findAngle1Falling());
		t2 = Math.toDegrees(findAngle2Falling());
		config.writeToMonitor( "T1: " + String.valueOf(t1), 4);
		config.writeToMonitor( "T2: " + String.valueOf(t2), 3);
		
		//find the slop 
			//assume origin is the heading of the beginning robot oritation
		double angleFromOrigin = ((t1 + t2)/2) ;
		config.writeToMonitor( "AFO " + String.valueOf(angleFromOrigin), 5);
		
		rotateToRelativly(-(t2-angleFromOrigin));

		/**
		 * TODO line reader need to be initialized here or else it has 
		 * a nullpointer exception. I think there is some problem withe 
		 * the order of which i initialize things in config.
		 */
		config.setLineReader(new LineReader(config));
		linereader = config.getLineReader();
		linereader.start();
		//give some time for color sensor to warm up 
		try {Thread.sleep(1000);	} catch (InterruptedException e) {config.writeToMonitor( "sleep interrupted!", 5); }
		
		//MOVE FORWARD UNTIL SEE A LINE 
		while( !config.getLineReader().isPassedLine() ){
			RobotConfiguration.LEFT_MOTOR.forward();
			RobotConfiguration.RIGHT_MOTOR.forward();
		} 
		config.stopMotor();
		
		//go back 12 cm for the wheel to be about the center
		config.getDriver().travel(-12);
		
		//rotate left to see line and set that line to be the origin
		//TODO make the rotation slower 

		rotateToRelatively(-90, true);
		
		while(true){
			if (!config.getLineReader().isPassedLine()){ /*do nothing */}
			else {//stop motor and break 
				config.stopMotor();
				break;
			}
		}
		
		config.setDriveComplete(true);
	}

	private double findAngle2Falling() {
		config.resetMotorSpeed();
		double result = -1 ;
		for (int i = 0 ; i <= deg ; i+=turningDeg ){
			rotateToRelativly(-turningDeg);
			if (config.getUsPoller().getDistance() < distance && config.getUsPoller().getDistance() > 0  ){

				config.stopMotor();
				result = currentCoordinate.getTheta();
				break ;
			}
			else {
				rotateToRelativly(-turningDeg);
			}
		}
		return result;
	}
			

	@SuppressWarnings("deprecation")
	private double findAngle1Falling() {
		double result = -1 ;
		for (int i = 0 ; i <= deg ; i+=turningDeg ){
			if (config.getUsPoller().getDistance() < distance && config.getUsPoller().getDistance() >0  ){

				config.stopMotor();
				result = currentCoordinate.getTheta();
				break ;
			}
			else {
				rotateToRelativly(turningDeg);
			}
		}
		rotateToRelativly(-Math.toDegrees(result)); //turn back deg first to avoid seeing the wall again rightaway
		return result;
	}
	

	@Override
	protected void handleObsticle() {
			// TODO Auto-generated method stub
		
	}

}
