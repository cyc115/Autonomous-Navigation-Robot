package lab4newArchetecture;
/**
 * this class wraps a ultrasonicListener and the distance value that it should be invoked.
 * this class is soly used by the UltrasonicPlanner and ultrasonicListener
 * 
 * @author yuechuan
 *
 */
public class UltrasonicSubscriberWrapper {
	
	/**
	 * the distance on which it will start invoking the listener 
	 */
	private int distanceOnInvoke = -1 ;
	/**
	 * If true, then the listener's method will be called repeatedly
	 */
	private boolean continueous; 
	/**
	set to true when this has been called.
	*/
	
	private boolean called ;
	
	private UltrasonicListener listener ;
	
	/**
	 * wraps a ultrasonic listener and a distance of which the listener will be invoked
	 * together into one object.so that the UltrasonicPoller can call the uListener 
	 * when distance is under disistanceOnInvoke
	 * @param uListener
	 * @param distanceOnInvoke
	 * @param continuous
	 */
	public UltrasonicSubscriberWrapper(UltrasonicListener uListener , int distanceOnInvoke,boolean continuous) {
		 this.listener = uListener;
		 this.distanceOnInvoke = distanceOnInvoke;
	}
	/**
	 * equals if usw.lister is the same object as this.listener
	 * @param usw
	 * @return true when equal 
	 */
	public boolean equals(UltrasonicSubscriberWrapper usw){
		if (usw instanceof UltrasonicSubscriberWrapper){
			return (usw.listener == this.listener) ? true : false ;
		}
		else return false;
	}
	/**
	 * equals if the uslistener is contains in this wrapper 
	 * @param usl
	 * @return true when equal 
	 */
	public boolean equals(UltrasonicListener usl){
		if (usl instanceof UltrasonicListener){
			return (this.listener == usl)? true : false ;
		}
		else return false;
	}
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}

	public void setDistanceOnInvoke(int distanceOnInvoke) {
		this.distanceOnInvoke = distanceOnInvoke;
	}

	public UltrasonicListener getListener() {
		return listener;
	}

	public void setListener(UltrasonicListener listener) {
		this.listener = listener;
	}
	
	public boolean isContinueous() {
		return continueous;
	}
	
	public void setContinueous(boolean continueous) {
		this.continueous = continueous;
	}

	public boolean isCalled() {
		return called;
	}
	/**
	 * must set to false when the listener finishes its ultrasonicDistance() method
	 * @param called
	 */
	public void setCalled(boolean called) {
		this.called = called;
	}

}
