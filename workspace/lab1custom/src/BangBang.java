import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

//@TODO add avg support 
//@TODO add printer 

public class BangBang extends Thread implements UltrasonicController {
	boolean quit = false ;
	//sensor motor turning angles 
	int leftAngle = -90 ;
	int rightAngle = 90;
	//initialize the distance to -1
	int leftDistance = -1 ;
	int topDisance = -1 ;
	//set up motors 
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private final NXTRegulatedMotor sensorMotor = Motor.B;
	UltrasonicSensor usSensor;
	
	//bandwidth and default motor speed 
	private static final int bandCenter = 20, bandWidth = 3;
	private static final int defaultSpeed = 300;
	private static final int increment = defaultSpeed/10;	//10% difference 
	private static final int turningConstant = 2 ; // the amount of turn if it tries to make a right turn is "turningConstant* increment"
	
	/**
	 * 
	 * @param sensor ultrasound sensor 
	 */
	public BangBang(SensorPort sensorPort) {
		usSensor = new UltrasonicSensor(sensorPort);
	}


	/**
	 * runs this method to beging the algo 
	 */
	public void run() {
		//start motoro the leftwall and top wall.
		 // look at the case on the sketched paper 
		leftMotor.setSpeed(defaultSpeed);
		rightMotor.setSpeed(defaultSpeed);
		
		while ( !quit ){
			//rot left and measure 
			sensorMotor.rotate(leftAngle);
			leftDistance = usSensor.getDistance();
			
			//rot top and measure
			sensorMotor.rotate(rightAngle);
			topDisance = usSensor.getDistance();
			act(leftDistance , topDisance);
		}
	}


	/**
	 * make actions based on the distance to the leftwall and top wall.
	 * look at the case on the sketched paper for more information 
	 * @param leftDist
	 * @param topDist
	 */
	private void act(int leftDist, int topDist) {
		int leftError = leftDist - bandCenter ;
		int topError = topDist - bandCenter ;
		
		//case 2 
		if ((Math.abs(leftError) < bandWidth) &&
			(topError > bandWidth) ) {
			//Do not change motor speed 
		}
		//case 1 
		else if ( (leftError > bandWidth) && 
				  (topError > bandWidth) ){
			//turn left 
			leftMotor.setSpeed(leftMotor.getSpeed()- increment );
			rightMotor.setSpeed(rightMotor.getSpeed() + increment);			
		} 
		//case 3
		else if ((leftError < bandWidth ) && 
				(topError > bandWidth) ) {
			//turn right 
			leftMotor.setSpeed(leftMotor.getSpeed()+ increment );
			rightMotor.setSpeed(rightMotor.getSpeed() - increment);	
		}
		//all other cases 
		else {
			//turn right fast
			leftMotor.setSpeed (leftMotor.getSpeed()+ increment *turningConstant );
			rightMotor.setSpeed(rightMotor.getSpeed() - increment*turningConstant);	
		}

	}


	@Override
	public String readUSDistance() {
		// TODO Auto-generated method stub
		return "Left: " + leftDistance +
				"top: " + topDisance;
	}

}