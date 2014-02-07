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
//		
//		t1 = Math.toDegrees(findAngle1());
//		t2 = Math.toDegrees(findAngle2());
//		config.writeToMonitor( "T1: " + String.valueOf(t1), 4);
//		config.writeToMonitor( "T2: " + String.valueOf(t2), 3);
//		
//		//find the slop 
//			//assume origin is the heading of the beginning robot oritation
//		double angleFromOrigin = ((t1 + t2)/2) ;
//		config.writeToMonitor( "AFO " + String.valueOf(angleFromOrigin), 5);
//		
//		rotateToRelativly(-(t2-angleFromOrigin));

		/**
		 * TODO line reader need to be initialized here or else it has 
		 * a nullpointer exception. I think there is some problem withe 
		 * the order of which i initialize things in config.
		 */
		
		config.setLineReader(new LineReader(config));
		linereader = config.getLineReader();
		linereader.start();
		
		//give some time for color sensor to warm up 
		try {Thread.sleep(1000);	} catch (InterruptedException e) {}
		
		//MOVE FORWARD UNTIL SEE A LINE 
		while(!config.getLineReader().isPassedLine()){
			RobotConfiguration.LEFT_MOTOR.forward();
			RobotConfiguration.RIGHT_MOTOR.forward();
		}

		config.setDriveComplete(true);
	}

	private double findAngle2() {
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
			

	private double findAngle1() {
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
		return result;
	}
	

	@Override
	protected void handleObsticle() {
			// TODO Auto-generated method stub
		
	}

}
