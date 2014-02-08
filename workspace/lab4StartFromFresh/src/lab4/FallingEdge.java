package lab4;

public class FallingEdge extends LocateOriginDriver implements Drivable {

	public FallingEdge(RobotConfiguration config) {
		super(config);
	}
	
	public void run(){
		//falling , see nothing -> see something 
		
//		t1 = Math.toDegrees(findAngle1Falling());
//		t2 = Math.toDegrees(findAngle2Falling());
//		config.writeToMonitor( "T1: " + String.valueOf(t1), 4);
//		config.writeToMonitor( "T2: " + String.valueOf(t2), 3);
//		
//		//find the slop 
//			//assume origin is the heading of the beginning robot oritation
//		double angleFromOrigin = ((t1 + t2)/2) ;
//		config.writeToMonitor( "AFO " + String.valueOf(angleFromOrigin), 5);
//		
//		rotateToRelatively(-(t2-angleFromOrigin));

		//do the rest of localization
		driveAndLocate();
	}
	
	private double findAngle2Falling() {
		config.resetMotorSpeed();
		double result = -1 ;
		for (int i = 0 ; i <= deg ; i+=turningDeg ){
			rotateToRelatively(-turningDeg);
			if (config.getUsPoller().getDistance() < distance && config.getUsPoller().getDistance() > 0  ){

				config.stopMotor();
				result = currentCoordinate.getTheta();
				break ;
			}
			else {
				rotateToRelatively(-turningDeg);
			}
		}
		return result;
	}
			

	private double findAngle1Falling() {
		double result = -1 ;
		for (int i = 0 ; i <= deg ; i+=turningDeg ){
			if (config.getUsPoller().getDistance() < distance && config.getUsPoller().getDistance() >0  ){

				config.stopMotor();
				result = currentCoordinate.getTheta();
				break ;
			}
			else {
				rotateToRelatively(turningDeg);
			}
		}
		rotateToRelatively(-Math.toDegrees(result)); //turn back deg first to avoid seeing the wall again rightaway
		return result;
	}

	@Override
	protected void handleObsticle() {
		// TODO Auto-generated method stub
		
	}
	
}
