package lab4;

import lejos.nxt.comm.RConsole;

public class RaisingEdge extends LocateOriginDriver implements Drivable{

	public RaisingEdge(RobotConfiguration config) {
		super(config);
	}

	@Override
	public void run() {
		t1 = Math.toDegrees(findAngle1());
		t2 = Math.toDegrees(findAngle2());
		
		config.writeToMonitor("T1 " + String.valueOf(t1), 04);
		config.writeToMonitor("T2 " + String.valueOf(t2), 03);
		
		//find slop 
		double angleFromOrigin = ((t1+t2)/2);
		config.writeToMonitor( "AFO " + String.valueOf(angleFromOrigin), 5);
		rotateToRelatively(-(t2-angleFromOrigin) + 180 );

		//do the rest of localization
		driveAndLocate();
	}
	/**
	 * find the frist angle with movements
	 * @return angle wrt to starting orientation in rad
	 */
	double findAngle1(){
		double result = -1 ;
		config.stopMotor();
		try{Thread.sleep(200);}catch(Exception e){}
		for (int i = 0 ; i <= deg ; i+=turningDeg ){
			int dist = config.getUsPoller().getDistance();
			if ( dist > distance ){
				config.stopMotor();
				RConsole.println(dist + "\n=======");
				result = currentCoordinate.getTheta();
				break ;
			}
			else {
				RConsole.println(dist+"");
				rotateToRelatively(turningDeg);
			}
		}
		rotateToRelatively(-Math.toDegrees(result)); //turn back deg first to avoid seeing the wall again rightaway
		return result;
	}
	/**
	 * find the second angle with movements
	 * @return angle wrt to starting orientation in rad
	 */
	double findAngle2(){
		config.resetMotorSpeed();
		double result = -1 ;
		for (int i = 0 ; i <= deg ; i+=turningDeg ){
			rotateToRelatively(-turningDeg);
			int dist = config.getUsPoller().getDistance() ;
			if (dist> distance ){

				config.stopMotor();
				result = currentCoordinate.getTheta();
				RConsole.println(dist+"\n==========\n");
				break ;
			}
			else {
				RConsole.println(dist+"");
				rotateToRelatively(-turningDeg);
			}
		}
		return result;
	}
	@Override
	protected void handleObsticle() {
		//no obstacle handling 
	}
	

}
