package lab4;

import lejos.nxt.comm.RConsole;

public class FallingEdge extends LocateOriginDriver implements Drivable {

	public FallingEdge(RobotConfiguration config) {
		super(config);
	}
	
	public void run(){
		//falling , see nothing -> see something 
		config.stopMotor();
		try{Thread.sleep(500);}catch(Exception e) {};

		//if this faces the wall then rotate 		
		while(config.getUsPoller().getDistance() < distance){
			config.getDriver().rotateToRelatively(45, false);
		}

		RConsole.println(config.getUsPoller().getDistance()+"");
		//SET CURRENT LOCATION TO ORIGIN
		config.getCurrentLocation().setTheta(0).setX(0).setY(0);
		
		//DO THE ANGLE FINDING
		t1 = Math.toDegrees(findAngle1Falling());
		t2 = Math.toDegrees(findAngle2Falling());
		config.writeToMonitor( "T1: " + String.valueOf(t1), 4);
		config.writeToMonitor( "T2: " + String.valueOf(t2), 3);
		
		//assume origin is the heading of the beginning robot oritation 
		/**
		 * angleFromOrigin is the angle between the two measured angles
		 */
		double angleFromOrigin = ((t1 + t2)/2) ;
		config.writeToMonitor( "AFO " + String.valueOf(angleFromOrigin), 5); 
		
		//rotate to be parallel to the diagonal line 
		config.getDriver().rotateToRelatively(-(t2-angleFromOrigin), false);
		//do the rest of localization
		driveAndLocate();
	}
	/**
	 * Do the actions to find the second angle 
	 * @return angle in radian
	 */
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
			
	/**
	 * Do the actions to find the first angle 
	 * @return angle in radian
	 */
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
		// not used in this lab 
		
	}
	
}
