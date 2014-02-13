package other;

import coreLib.UltrasonicListener;

/**
 * TODO incomplete class 
 * contains the main portion of movements from the current location to finding the brick.
 * object of this class listens to the ultrasonicPoller 
 * @author yuechuan
 *
 */
public class FindItem implements UltrasonicListener{
	
	private int distanceOnInvoke ;

	private boolean continuous;
	private boolean called ;

	@Override
	public void ultrasonicDistance(int distanceFromObsticle) {
		// TODO Auto-generated method stub
		
	}

	
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}

	public UltrasonicListener setDistanceOnInvoke(int distanceOnInvoke) {
		this.distanceOnInvoke = distanceOnInvoke;
		return this;
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
		return this;
	}


}
