package lab4newArchetecture;

/**
 * host a public method that does the localization
 * @author yuechuan
 *
 */
public class Localize implements UltrasonicListener{
	//the following 3 is used for the UltrasonicListener
	private int distanceOnInvoke;
	private boolean continuous;
	private boolean called ;
	double angle1 , angle2 , middle ;
	boolean secondAngle = false ;	// if this is called a second time then turn back 

	
	public Localize(int distanceOnInvoke , boolean continuous){
		this.distanceOnInvoke = distanceOnInvoke;
		this.continuous = continuous;
	}
	@Override
	public void ultrasonicDistance(int distanceFromObsticle) {
		Driver.motorStop();
		if (!secondAngle){
			angle1 = AbstractConfig.getInstance().getCurrentLocation().getTheta();
			LCDWriter.getInstance().writeToScreen("ang1" + angle1 , 6);
			//rotate back 
			Driver.getInstance().rotateToRelatively(-360, true);
			try {Thread.sleep(400);} catch ( Exception e ){};
			secondAngle = true;
			called = false;
		}
		else {
			angle2 = AbstractConfig.getInstance().getCurrentLocation().getTheta();
			LCDWriter.getInstance().writeToScreen("ang2" + angle1 , 5);
			//rotate to middle 
			double angleFromOrigin = ((angle1 + angle2)/2) ;
			LCDWriter.getInstance().writeToScreen("AFO " +angleFromOrigin , 0);
			continuous = true;
		}
	}
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}

	public UltrasonicListener setDistanceOnInvoke(int distanceOnInvoke) {
		this.distanceOnInvoke = distanceOnInvoke;
		return this ;
	}

	public boolean isContinuous() {
		return continuous;
	}

	public UltrasonicListener setContinuous(boolean continuous) {
		this.continuous = continuous;
		return this;
	}

	public boolean isCalled() {
		return called;
	}

	public UltrasonicListener setCalled(boolean called) {
		this.called = called;
		return this ;
	}
}
