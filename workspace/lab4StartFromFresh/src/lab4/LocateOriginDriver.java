package lab4;

public class LocateOriginDriver extends Driver implements Drivable {
	int deg = 360 ;
	int turningDeg = 5;
	int distance = 25 ; //angle to use when distance from sensor falls below this 
	double t1 = -1 , t2 = -1; //angle 1 and angle 2
	
	public LocateOriginDriver(RobotConfiguration config) {
		super(config);
		
	}
	
	public void run(){
		//falling , see nothing -> see something 

		
		t1 = findAngle1();
		t2 = findAngle2();
		
		config.writeToMonitor( "T1: " + String.valueOf(t1), 4);
		config.writeToMonitor( "T2: " + String.valueOf(t2), 5);

		
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
